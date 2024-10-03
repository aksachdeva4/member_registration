package org.skrmnj.membermanagement.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skrmnj.membermanagement.bean.SortingOptions;
import org.skrmnj.membermanagement.controller.beans.MemberListRequest;
import org.skrmnj.membermanagement.controller.beans.MemberRegistrationRequest;
import org.skrmnj.membermanagement.controller.beans.MembersListResponse;
import org.skrmnj.membermanagement.domain.Address;
import org.skrmnj.membermanagement.domain.Member;
import org.skrmnj.membermanagement.domain.repository.AddressRepository;
import org.skrmnj.membermanagement.domain.repository.MemberRepository;
import org.skrmnj.membermanagement.utilities.Utils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.skrmnj.membermanagement.utilities.Dictionary.BLANK;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService {

    private static final String QUERY_MEMBER_FILTER_FETCH = "select m.*, count(adm.userid) additional_members_count from members m Left join members adm on adm.primary_user_id=m.userid where ( length(:firstName) < 1 or UPPER(m.first_name) like concat('%',:firstName,'%') ) and ( length(:lastName) < 1 or UPPER(m.last_name) like concat('%',:lastName,'%') ) group by m.userid ";
    private static final String QUERY_ORDER_BY = " ORDER BY %s %s ";
    private static final String QUERY_LIMIT = " limit :startIndex, :pageSize ";
    private static final String QUERY_MEMBER_FILTER_COUNT = "select count(*) from members m where ( length(:firstName) < 1 or UPPER(m.first_name) like concat('%',:firstName,'%') ) and ( length(:lastName) < 1 or UPPER(m.last_name) like concat('%',:lastName,'%') )";
    private static final Map<String, String> columnMapping = new HashMap<>();

    static {
        columnMapping.put("id", "userid");
        columnMapping.put("firstName", "first_name");
        columnMapping.put("lastName", "last_name");
        columnMapping.put("additionalMembersCount", "additional_members_count");
    }

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

        String selectQuery = QUERY_MEMBER_FILTER_FETCH.concat(getOrderBy(request.getSortingOption())).concat(QUERY_LIMIT);

        List<Member> memberList = npjt.queryForStream(selectQuery, params, (rs, rowNum) -> {
            Member m = new Member();
            m.setId(rs.getInt("userid"));
            m.setFirstName(rs.getString("first_name"));
            m.setLastName(rs.getString("last_name"));
            m.setPhoneNumber(rs.getString("phone_number"));
            m.setCountryCode(rs.getString("country_code"));
            m.setAdditionalMembersCount(rs.getInt("additional_members_count"));
            return m;
        }).toList();

        long total = Objects.requireNonNullElse(npjt.queryForObject(QUERY_MEMBER_FILTER_COUNT, params, Long.class), 0L);

        MembersListResponse response = new MembersListResponse();
        response.getPagination().setCurrentPage(request.getPagination().getLoadPage()).setLoadPage(request.getPagination().getLoadPage()).setRowsPerPage(pageSize).setTotalRows(total);
        response.setMembers(memberList);

        return response;
    }

    private String getOrderBy(SortingOptions sortingOptions) {
        String columnName = columnMapping.get("id");
        String direction = "asc";

        if (sortingOptions != null && columnMapping.containsKey(sortingOptions.getColumnName()) && Utils.arrayContains(sortingOptions.getSortingDirection(), "asc", "desc")) {
            columnName = columnMapping.get(sortingOptions.getColumnName());
            direction = sortingOptions.getSortingDirection();
        }

        return String.format(QUERY_ORDER_BY, columnName, direction);
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
