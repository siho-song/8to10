package show.schedulemanagement.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import show.schedulemanagement.exception.AuthException;
import show.schedulemanagement.exception.ExceptionCode;
import show.schedulemanagement.exception.InvalidTokenException;
import show.schedulemanagement.exception.UserAuthenticationException;
import show.schedulemanagement.handler.AuthFilterExceptionHandler;
import show.schedulemanagement.provider.TokenProvider;
import show.schedulemanagement.service.auth.MemberDetailsService;
import show.schedulemanagement.utils.BearerAuthorizationUtils;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberDetailsService memberDetailsService;
    private final BearerAuthorizationUtils bearerUtils;
    private final TokenProvider tokenProvider;
    private final AuthFilterExceptionHandler authFilterExceptionHandler;

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/",
            "/signup",
            "/error",
            "/login",
            "/renew",
            "/static/**",
            "/js/**",
            "/css/**",
            "/templates/**",
            "/images/**",
            "/signup/**",
            "/favicon**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (isExcludeUrl(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = bearerUtils.extractToken(authHeader);

        try {
            if(tokenProvider.isValidToken(token)){
                String loginId = tokenProvider.getUserIdFromToken(token);
                log.debug("loginId : {}", loginId);
                UserDetails userDetails = memberDetailsService.loadUserByUsername(loginId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
            else {
                throw new UserAuthenticationException(ExceptionCode.INVALID_ACCESS_TOKEN);
            }
        } catch (AuthenticationException e) {
            authFilterExceptionHandler.handleException(response,e);
        }
    }

    private boolean isExcludeUrl(String uri) {
        return EXCLUDE_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }
}
