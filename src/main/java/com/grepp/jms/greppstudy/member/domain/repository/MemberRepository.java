package com.grepp.jms.greppstudy.member.domain.repository;

import com.grepp.jms.greppstudy.member.domain.model.Member;
import java.util.List;

public interface MemberRepository {
    List<Member> findAll();

    Member save(Member member);

    boolean findByPhone(String phone);

    Member findByEmail(String email);
}
