package show.schedulemanagement.service.signup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.repository.member.MemberRepository;
import show.schedulemanagement.dto.signup.SignUpRequest;

@Service
@RequiredArgsConstructor
public class SignUpValidationService {

    private final MemberRepository memberRepository;

    public Boolean isValidSignUp(SignUpRequest signUpRequest) {
        return !isDuplicatedEmail(signUpRequest.getEmail()) && !isDuplicatedNickname(signUpRequest.getNickname());
    }

    public Boolean isDuplicatedEmail(String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        return member != null;
    }

    public Boolean isDuplicatedNickname(String nickname){
        Member member = memberRepository.findByNickname(nickname).orElse(null);
        return member != null;
    }
}
