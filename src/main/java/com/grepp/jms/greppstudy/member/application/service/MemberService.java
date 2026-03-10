package com.grepp.jms.greppstudy.member.application.service;

import com.grepp.jms.greppstudy.member.application.usecase.MemberUseCase;
import com.grepp.jms.greppstudy.member.domain.model.Member;
import com.grepp.jms.greppstudy.member.domain.repository.MemberRepository;
import com.grepp.jms.greppstudy.member.presentation.dto.request.LoginRequest;
import com.grepp.jms.greppstudy.member.presentation.dto.request.MemberEnrollRequest;
import com.grepp.jms.greppstudy.member.presentation.dto.res.MemberAdminResponse;
import com.grepp.jms.greppstudy.member.presentation.dto.res.MemberResponse;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService implements MemberUseCase {
    public final MemberRepository memberRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream().map(this::changeMemberResType).toList();
    }

    @Override
    public List<MemberAdminResponse> findAdmAll() {
        return memberRepository.findAll().stream().map(this::changeMemberAdmResType).toList();
    }

    @Transactional
    @Override
    public MemberResponse save(MemberEnrollRequest memberEnrollRequest) {
        if(!memberRepository.findByPhone(memberEnrollRequest.phone())){

            SecureRandom random = new SecureRandom();
            byte[] saltkey = random.generateSeed(8);

            Member member =Member.create(memberEnrollRequest.email(), memberEnrollRequest.name(), memberEnrollRequest.address(),
                    memberEnrollRequest.status(), memberEnrollRequest.password(), memberEnrollRequest.phone());
            member.setSaltKey(Base64.getEncoder().encodeToString(saltkey));
            log.info("saltkey : {}", member.getSaltKey());
            member.setPassword(encoder.encode(memberEnrollRequest.password()+member.getSaltKey()));

            memberRepository.save(member);
            return new MemberResponse(
                    member.getId(), member.getName(), member.getAddress());
        }else{
            //TODO: throw
        }
        return null;
    }

    @Override
    public Boolean login(LoginRequest login) {
        Member member = memberRepository.findByEmail(login.email());
        if(encoder.matches(login.password()+member.getSaltKey(), member.getPassword())){
            return true;
        }
        return false;
    }

    private MemberResponse changeMemberResType(Member member){
        return new MemberResponse(
                member.getId(), member.getName(), member.getAddress()
        );
    }
    private MemberAdminResponse changeMemberAdmResType(Member member){
        return new MemberAdminResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                member.getAddress(),
                member.getStatus(),
                member.getRegId(),
                member.getRegDt(),
                member.getModifyId(),
                member.getModifyDt(),
                member.getFlag()
        );
    }
}
