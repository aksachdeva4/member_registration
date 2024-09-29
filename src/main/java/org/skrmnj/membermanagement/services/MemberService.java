package org.skrmnj.membermanagement.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skrmnj.membermanagement.controller.beans.MemberListRequest;
import org.skrmnj.membermanagement.controller.beans.MemberRegistrationRequest;
import org.skrmnj.membermanagement.controller.beans.MembersListResponse;
import org.skrmnj.membermanagement.domain.Address;
import org.skrmnj.membermanagement.domain.Member;
import org.skrmnj.membermanagement.domain.repository.AddressRepository;
import org.skrmnj.membermanagement.domain.repository.MemberRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.skrmnj.membermanagement.utilities.Dictionary.BLANK;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService {

    private static final String QUERY_MEMBER_FILTER_FETCH = "select m.*, count(adm.userid) additional_members_count from members m Left join members adm on adm.primary_user_id=m.userid where ( length(:firstName) < 1 or UPPER(m.first_name) like concat('%',:firstName,'%') ) and ( length(:lastName) < 1 or UPPER(m.last_name) like concat('%',:lastName,'%') ) group by m.userid limit :startIndex, :pageSize ";
    private static final String QUERY_MEMBER_FILTER_COUNT = "select count(*) from members m where ( length(:firstName) < 1 or UPPER(m.first_name) like concat('%',:firstName,'%') ) and ( length(:lastName) < 1 or UPPER(m.last_name) like concat('%',:lastName,'%') )";
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final NamedParameterJdbcTemplate npjt;

    @Transactional(readOnly = true)
    public MembersListResponse getAllMembersFromLastName(MemberListRequest request) {

        long startIndex = 0;
        long pageSize = 20;

        if (null != request.getPagination()) {
            if (request.getPagination().getRowsPerPage() > 0) {
                pageSize = request.getPagination().getRowsPerPage();
            }
            if (request.getPagination().getLoadPage() > 0) {
                startIndex = (request.getPagination().getLoadPage() - 1) * pageSize;
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("firstName", Objects.requireNonNullElse(request.getFirstName(), BLANK).trim());
        params.put("lastName", Objects.requireNonNullElse(request.getLastName(), BLANK).trim());
        params.put("startIndex", startIndex);
        params.put("pageSize", pageSize);

        List<Member> memberList = npjt.queryForStream(QUERY_MEMBER_FILTER_FETCH, params, (rs, rowNum) -> {
            Member m = new Member();
            m.setId(rs.getInt("userid"));
            m.setFirstName(rs.getString("first_name"));
            m.setLastName(rs.getString("last_name"));
            m.setPhoneNumber(rs.getString("phone_number"));
            m.setCountryCode(rs.getString("country_code"));
            m.setAdditionalMembersCount(rs.getInt("additional_members_count"));
            return m;
        }).toList();

        long total = npjt.queryForObject(QUERY_MEMBER_FILTER_COUNT, params, Long.class);

        MembersListResponse response = new MembersListResponse();
        response.getPagination().setCurrentPage(request.getPagination().getLoadPage()).setLoadPage(request.getPagination().getLoadPage()).setRowsPerPage(pageSize).setTotalRows(total);
        response.setMembers(memberList);

        return response;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public boolean registerMember(MemberRegistrationRequest memberRegistrationRequest) {

        Member primaryMember = memberRepository.saveAndFlush(convertToMember(memberRegistrationRequest, true, null));
        addressRepository.saveAndFlush(convertToAddress(memberRegistrationRequest, primaryMember));

        List<Member> additionalMembers = Objects.requireNonNullElse(memberRegistrationRequest.getAdditionalMembers(), new ArrayList<MemberRegistrationRequest>()).stream().map(x -> convertToMember(x, false, primaryMember.getId())).toList();

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
