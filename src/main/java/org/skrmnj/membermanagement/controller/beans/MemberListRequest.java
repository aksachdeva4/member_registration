package org.skrmnj.membermanagement.controller.beans;

import lombok.Data;
import org.skrmnj.membermanagement.bean.Pagination;
import org.skrmnj.membermanagement.bean.SortingOption;

@Data
public class MemberListRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean isMember = false;
    private String initiatedBy;
    private Address address;

    private Pagination pagination;
    private SortingOption sortingOption;

}
