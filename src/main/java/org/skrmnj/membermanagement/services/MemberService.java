package org.skrmnj.membermanagement.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skrmnj.membermanagement.controller.beans.MemberListRequest;
import org.skrmnj.membermanagement.controller.beans.MemberRegistrationRequest;
import org.skrmnj.membermanagement.controller.beans.MembersListResponse;
import org.skrmnj.membermanagement.domain.Address;
import org.skrmnj.membermanagement.domain.AddressRepository;
import org.skrmnj.membermanagement.domain.Member;
import org.skrmnj.membermanagement.domain.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public MembersListResponse getAllMembersFromLastName(MemberListRequest request) {

        int pageNumber = 0;
        int pageSize = 20;

        if (null != request.getPagination()) {
            if (request.getPagination().getLoadPage() > 0) {
                pageNumber = request.getPagination().getLoadPage();
            }
            if (request.getPagination().getRowsPerPage() > 0) {
                pageSize = request.getPagination().getRowsPerPage();
            }
        }

        Page<Member> members = memberRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));

        MembersListResponse response = new MembersListResponse();
        response.getPagination().setCurrentPage(pageNumber).setLoadPage(pageNumber).setRowsPerPage(pageSize).setTotalPages(members.getTotalPages()).setTotalRows(members.getTotalElements());
        response.setMembers(members.getContent());
        return response;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public boolean registerMember(MemberRegistrationRequest memberRegistrationRequest) {

        Member primaryMember = memberRepository.saveAndFlush(convertToMember(memberRegistrationRequest, true, null));
        addressRepository.saveAndFlush(convertToAddress(memberRegistrationRequest, primaryMember));

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

    private Address convertToAddress(MemberRegistrationRequest memberRegistrationRequest, Member primaryUser) {
        Address address = new Address();

        address.setPrimaryPerson(primaryUser);
        address.setStreetName(memberRegistrationRequest.getAddress().getStreetName());
        address.setCity(memberRegistrationRequest.getAddress().getCity());
        address.setAptOrUnitNo(memberRegistrationRequest.getAddress().getAptOrUnitNo());
        address.setState(memberRegistrationRequest.getAddress().getState());
        address.setCountry(memberRegistrationRequest.getAddress().getCountry());
        address.setZipCode(memberRegistrationRequest.getAddress().getZipcode());

        return address;
    }

}
