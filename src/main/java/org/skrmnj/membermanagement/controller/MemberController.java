package org.skrmnj.membermanagement.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skrmnj.membermanagement.controller.beans.MemberListRequest;
import org.skrmnj.membermanagement.controller.beans.MemberRegistrationRequest;
import org.skrmnj.membermanagement.controller.beans.MembersListResponse;
import org.skrmnj.membermanagement.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/find")
    public ResponseEntity<MembersListResponse> getAllMembersFromLastName(@RequestBody MemberListRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAllMembersFromLastName(request));
    }

    @PostMapping("/register")
    public boolean registerMember(@RequestBody MemberRegistrationRequest memberRegistrationRequest) {
        return memberService.registerMember(memberRegistrationRequest);
    }

}
