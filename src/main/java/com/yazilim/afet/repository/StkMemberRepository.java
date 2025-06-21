package com.yazilim.afet.repository;

import com.yazilim.afet.entity.StkMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StkMemberRepository extends JpaRepository<StkMember, Long> {
    boolean existsByTcAndStkId(String tc, Long stkId);
}
