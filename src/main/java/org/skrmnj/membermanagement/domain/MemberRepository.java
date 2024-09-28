package org.skrmnj.membermanagement.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Page<Member> findAll(Pageable pageable);


    List<Member> findAllByLastNameOrFirstName(String lastName, String firstName);

    Page<Member> findAllByLastNameOrFirstName(String lastName, String firstName, Pageable pageable);

}