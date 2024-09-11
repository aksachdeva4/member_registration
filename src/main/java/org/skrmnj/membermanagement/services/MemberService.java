package org.skrmnj.membermanagement.services;

import lombok.AllArgsConstructor;
import org.skrmnj.membermanagement.controller.beans.MemberRegistrationRequest;
import org.skrmnj.membermanagement.domain.Member;
import org.skrmnj.membermanagement.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getAllMembersFromLastName(String lastName, String firstName) {
        return memberRepository.findAllByLastNameOrFirstName(lastName, firstName);
    }

    public boolean registerMember(MemberRegistrationRequest memberRegistrationRequest) {

        Member primaryMember = memberRepository.saveAndFlush(convertToMember(memberRegistrationRequest, true, null));

        List<Member> additionalMembers = Objects.requireNonNullElse(memberRegistrationRequest.getAdditionalMembers(), new ArrayList<MemberRegistrationRequest>())
                .stream().map(x -> convertToMember(x, false, primaryMember.getId())).toList();

        memberRepository.saveAllAndFlush(additionalMembers);

        return true;
    }

    private Member convertToMember(MemberRegistrationRequest memberRegistrationRequest, boolean isPrimary, Integer primaryMemberId) {
        Member member = new Member();
        member.setFirstName(memberRegistrationRequest.getFirstName());
        member.setLastName(memberRegistrationRequest.getLastName());
        member.setEmailId(memberRegistrationRequest.getEmail());
        member.setPhoneNumber(memberRegistrationRequest.getPhoneNumber());
        member.setIsMember(memberRegistrationRequest.isMember() ? 'Y' : 'N');
        if ('Y' == member.getIsMember()) {
            member.setInitiatedBy(memberRegistrationRequest.getInitiatedBy());
        }

        if (primaryMemberId != null) {
            member.setPrimaryUserId(primaryMemberId);
        }

        member.setIsPrimary(isPrimary ? 'Y' : 'N');

        return member;
    }

}
