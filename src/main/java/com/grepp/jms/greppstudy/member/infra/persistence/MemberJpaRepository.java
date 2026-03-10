package com.grepp.jms.greppstudy.member.infra.persistence;

import com.grepp.jms.greppstudy.member.domain.model.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByPhone(String phone);
    Optional<Member> findByEmail(String email);
}
