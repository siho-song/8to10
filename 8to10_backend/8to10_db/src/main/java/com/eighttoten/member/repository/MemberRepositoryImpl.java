package com.eighttoten.member.repository;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.member.domain.NewMember;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public void save(NewMember newMember) {
        memberJpaRepository.save(MemberEntity.from(newMember));
    }

    @Override
    public void update(Member member) {
        MemberEntity entity = memberJpaRepository.findById(member.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
        entity.update(member);
    }

    @Override
    public void deleteById(Long id) {
        memberJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id).map(MemberEntity::toMember);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email).map(MemberEntity::toMember);
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return memberJpaRepository.findByNickname(nickname).map(MemberEntity::toMember);
    }

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll().stream().map(MemberEntity::toMember).toList();
    }
}