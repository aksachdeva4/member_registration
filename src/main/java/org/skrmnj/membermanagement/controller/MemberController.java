package org.skrmnj.membermanagement.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skrmnj.membermanagement.controller.beans.MemberRegistrationRequest;
import org.skrmnj.membermanagement.domain.Member;
import org.skrmnj.membermanagement.services.MemberService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/find-by-last-name")
    public List<Member> getAllMembersFromLastName(@RequestParam(required = false) String lastName, @RequestParam(required = false) String firstName) {
        log.debug("Test");

        if (!StringUtils.hasText(lastName) && !StringUtils.hasText(firstName)) {
            return new ArrayList<>();
        }

        return memberService.getAllMembersFromLastName(lastName, firstName);
    }

    @GetMapping("/get-all")
    public List<Member> getAllMembers() {
        log.debug("Test");
        return memberService.getAllMembers();
    }

    @PostMapping("/register")
    public boolean registerMember(@RequestBody MemberRegistrationRequest memberRegistrationRequest) {
        return memberService.registerMember(memberRegistrationRequest);
    }

}
