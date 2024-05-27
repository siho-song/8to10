package show.schedulemanagement.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.member.MemberDto;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.security.service.MemberDetailsService;
import show.schedulemanagement.security.utils.TokenUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenUtils tokenUtils;
    private final MemberDetailsService memberDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        String token = tokenUtils.generateJwtToken(new show.schedulemanagement.security.dto.LoginMemberDto(email));

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        setSession(request, email);
        response.sendRedirect("/home");
    }

    private void setSession(HttpServletRequest request, String email) {
        Member member = ((MemberDetailsDto) memberDetailsService.loadUserByUsername(email)).getMember();
        request.getSession().setAttribute("member", MemberDto.from(member));
    }
}