package org.skrmnj.membermanagement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "membermanagement")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "primary_person_id", nullable = false)
    private Member primaryPerson;

    @Size(max = 50)
    @Column(name = "street_name", length = 50)
    private String streetName;

    @Size(max = 45)
    @Column(name = "apt_or_unit_no", length = 45)
    private String aptOrUnitNo;

    @Size(max = 45)
    @NotNull
    @Column(name = "city", nullable = false, length = 45)
    private String city;

    @Size(max = 45)
    @NotNull
    @Column(name = "state", nullable = false, length = 45)
    private String state;

    @Size(max = 45)
    @Column(name = "zip_code", length = 45)
    private String zipCode;

    @Size(max = 45)
    @NotNull
    @ColumnDefault("'USA'")
    @Column(name = "country", nullable = false, length = 45)
    private String country;

}