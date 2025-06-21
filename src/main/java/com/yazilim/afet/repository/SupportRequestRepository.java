package com.yazilim.afet.repository;

import com.yazilim.afet.dto.SupportSummaryDTO;
import com.yazilim.afet.entity.SupportRequest;
import com.yazilim.afet.enums.SupportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {

    @Query("SELECT new com.yazilim.afet.dto.SupportSummaryDTO(aidType.name, SUM(srt.quantity)) " +
            "FROM SupportRequestType srt " +
            "JOIN srt.aidType aidType " +
            "JOIN srt.supportRequest sr " +
            "WHERE sr.location.id = :locationId " +
            "GROUP BY aidType.name " +
            "ORDER BY aidType.name")
    List<SupportSummaryDTO> getSupportSummaryByLocationId(@Param("locationId") Long locationId);

    @Query(value = """
            SELECT sr.id,
                   sr.description,
                   sr.created_at,
                   sr.status,
                   srt.aid_type_id,
                   srt.quantity,
                   sr.person_id
            FROM support_requests sr
            JOIN support_request_types srt ON sr.id = srt.support_request_id
            WHERE sr.location_id = :locationId
            ORDER BY sr.created_at DESC, sr.id
            """, nativeQuery = true)
    List<Object[]> getSupportRequestDetailsByLocationId(@Param("locationId") Long locationId);

    Optional<SupportRequest> findByIdAndPerson_Id(Long id, Long personId);

    List<SupportRequest> findAllByStatus(SupportStatus status);
    
    @Query("SELECT sr FROM SupportRequest sr WHERE sr.person.id = :personId ORDER BY sr.createdAt DESC")
    List<SupportRequest> findByPersonIdOrderByCreatedAtDesc(@Param("personId") Long personId);
    
    @Query("SELECT sr FROM SupportRequest sr WHERE sr.status = :status ORDER BY sr.createdAt ASC")
    List<SupportRequest> findByStatusOrderByCreatedAtAsc(@Param("status") SupportStatus status);
}