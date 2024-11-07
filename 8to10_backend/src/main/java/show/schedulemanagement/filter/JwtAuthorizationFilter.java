package show.schedulemanagement.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import show.schedulemanagement.provider.TokenProvider;
import show.schedulemanagement.service.auth.MemberDetailsService;
import show.schedulemanagement.utils.BearerAuthorizationUtils;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberDetailsService memberDetailsService;
    private final BearerAuthorizationUtils bearerUtils;
    private final TokenProvider tokenProvider;

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
                throw new JwtException("유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private boolean isExcludeUrl(String uri) {
        return EXCLUDE_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        String logMessage = jsonResponseWrapper(e).getString("message");
        log.error(logMessage, e);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter printWriter = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", true);
        jsonObject.put("message", "로그인 에러");

        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }

    private JSONObject jsonResponseWrapper(Exception e) {
        String resultMessage = getResultMessage(e);

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "401");
        jsonMap.put("message", resultMessage);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        log.error(resultMessage, e);
        return jsonObject;
    }

    private String getResultMessage(Exception e) {
        if (e instanceof ExpiredJwtException) {
            return "TOKEN Expired";
        } else if (e instanceof SignatureException) {
            return "TOKEN SignatureException Login";
        } else if (e instanceof JwtException) {
            return "TOKEN Parsing JwtException";
        } else {
            return "OTHER ERROR";
        }
    }
}