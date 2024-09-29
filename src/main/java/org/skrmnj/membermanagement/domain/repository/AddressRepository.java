package org.skrmnj.membermanagement.domain.repository;

import org.skrmnj.membermanagement.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}