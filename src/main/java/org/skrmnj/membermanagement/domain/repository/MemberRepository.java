package org.skrmnj.membermanagement.domain.repository;

import org.skrmnj.membermanagement.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}