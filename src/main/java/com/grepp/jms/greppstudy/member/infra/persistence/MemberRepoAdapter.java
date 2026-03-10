package com.grepp.jms.greppstudy.member.infra.persistence;

import com.grepp.jms.greppstudy.member.domain.model.Member;
import com.grepp.jms.greppstudy.member.domain.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepoAdapter implements MemberRepository {
    public final MemberJpaRepository memberJpaRepository;
    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll();
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public boolean findByPhone(String phone) {
        return memberJpaRepository.findByPhone(phone).isPresent();
    }

    @Override
    public Member findByEmail(String email) {
        return memberJpaRepository.findByEmail(email).orElseThrow();
    }
}
