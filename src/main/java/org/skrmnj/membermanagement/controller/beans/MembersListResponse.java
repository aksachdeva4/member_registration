package org.skrmnj.membermanagement.controller.beans;

import lombok.Data;
import org.skrmnj.membermanagement.bean.Pagination;
import org.skrmnj.membermanagement.domain.Member;

import java.util.List;

@Data
public class MembersListResponse {

    private List<Member> members;
    private Pagination pagination= new Pagination();


}
