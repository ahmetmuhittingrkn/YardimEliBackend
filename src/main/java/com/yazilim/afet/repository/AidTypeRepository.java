package com.yazilim.afet.repository;

import com.yazilim.afet.entity.AidType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AidTypeRepository extends JpaRepository<AidType, Long> {
}
