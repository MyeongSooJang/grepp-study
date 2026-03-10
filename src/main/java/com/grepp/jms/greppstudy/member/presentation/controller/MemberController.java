package com.grepp.jms.greppstudy.member.presentation.controller;

import com.grepp.jms.greppstudy.member.application.usecase.MemberUseCase;
import com.grepp.jms.greppstudy.member.presentation.dto.request.LoginRequest;
import com.grepp.jms.greppstudy.member.presentation.dto.request.MemberEnrollRequest;
import com.grepp.jms.greppstudy.member.presentation.dto.res.MemberAdminResponse;
import com.grepp.jms.greppstudy.member.presentation.dto.res.MemberResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@Tag(name = "Member", description = "사용자 CRUD API")
@RequiredArgsConstructor
public class MemberController {
    public final MemberUseCase memberUseCase;
    @GetMapping("findAll")
    public ResponseEntity<List<MemberResponse>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(memberUseCase.findAll());
    }
    @GetMapping("findAdmAll")
    public ResponseEntity<List<MemberAdminResponse>> getAdmAll(){
        return ResponseEntity.status(HttpStatus.OK).body(memberUseCase.findAdmAll());
    }
    @PostMapping("join")
    public ResponseEntity<MemberResponse> join(@RequestBody MemberEnrollRequest memberEnrollRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(memberUseCase.save(memberEnrollRequest));
    }

    @PostMapping("login")
    public ResponseEntity<Boolean> login(@RequestBody LoginRequest login){
        return ResponseEntity.status(HttpStatus.OK).body(memberUseCase.login(login));
    }
}
