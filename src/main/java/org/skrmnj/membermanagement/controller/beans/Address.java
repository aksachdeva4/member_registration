package org.skrmnj.membermanagement.controller.beans;

import lombok.Data;

@Data
public class Address {

    private String streetName;
    private String aptOrUnitNo;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
