package org.skrmnj.membermanagement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Builder
@Getter
@Setter
@Entity
@Table(name = "members", schema = "membermanagement")
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Size(max = 150)
    @Column(name = "email_id", length = 150)
    private String emailId;

    @Size(max = 15)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Size(max = 5)
    @NotNull
    @Column(name = "country_code", nullable = false, length = 5)
    private String countryCode;

    @NotNull
    @ColumnDefault("'Y'")
    @Column(name = "is_primary", nullable = false)
    private Character isPrimary;

    @Column(name = "primary_user_id")
    private Integer primaryUserId;

    @ColumnDefault("'N'")
    @Column(name = "is_member")
    private Character isMember;

    @Size(max = 3)
    @Column(name = "initiated_by", length = 3)
    private String initiatedBy;

}