package com.eighttoten.member.service;

import com.eighttoten.exception.BadRequestException;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.member.domain.NewMember;
import com.eighttoten.support.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(NewMember newMember){
        if (!isValidSignUp(newMember)){
            throw new BadRequestException(ExceptionCode.SIGNUP_FAILED);
        }
        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
        memberRepository.save(newMember);
    }

    public boolean isDuplicatedEmail(String email){
        Member memberEntity = memberRepository.findByEmail(email)
                .orElse(null);

        return memberEntity != null;
    }

    public boolean isDuplicatedNickname(String nickname){
        Member member = memberRepository.findByNickname(nickname)
                .orElse(null);

        return member != null;
    }

    private boolean isValidSignUp(NewMember newMember) {
        return !isDuplicatedEmail(newMember.getEmail()) && !isDuplicatedNickname(newMember.getNickname());
    }
}