package org.skrmnj.membermanagement.controller.beans;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberRegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean isMember = false;
    private String initiatedBy;
    private Address address;

    private List<MemberRegistrationRequest> additionalMembers = new ArrayList<>();
}
