package com.grepp.jms.greppstudy.member.application.usecase;

import com.grepp.jms.greppstudy.member.application.dto.TokenResponse;
import com.grepp.jms.greppstudy.member.presentation.dto.request.LoginRequest;
import com.grepp.jms.greppstudy.member.presentation.dto.request.MemberEnrollRequest;
import com.grepp.jms.greppstudy.member.presentation.dto.res.MemberAdminResponse;
import com.grepp.jms.greppstudy.member.presentation.dto.res.MemberResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface MemberUseCase {
    List<MemberResponse> findAll();
    List<MemberAdminResponse> findAdmAll();

    MemberResponse save(MemberEnrollRequest memberEnrollRequest);

    TokenResponse login(LoginRequest login) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
