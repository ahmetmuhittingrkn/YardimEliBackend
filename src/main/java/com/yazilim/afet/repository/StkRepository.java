package com.yazilim.afet.repository;

import com.yazilim.afet.entity.Stk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StkRepository extends JpaRepository<Stk, Long> {
    Optional<Stk> findByName(String name);
}
