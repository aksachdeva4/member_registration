package org.skrmnj.membermanagement.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    List<Member> findAllByLastNameOrFirstName(String lastName, String firstName);

}