package com.yazilim.afet.repository;

import com.yazilim.afet.dto.AidSummaryDTO;
import com.yazilim.afet.entity.AidRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AidRequestRepository extends JpaRepository<AidRequest, Long> {

    @Query("SELECT new com.yazilim.afet.dto.AidSummaryDTO(aidType.name, SUM(art.quantity)) " +
            "FROM AidRequestType art " +
            "JOIN art.aidType aidType " +
            "JOIN art.aidRequest ar " +
            "WHERE ar.location.id = :locationId " +
            "GROUP BY aidType.name")
    List<AidSummaryDTO> getAidSummaryByLocationId(@Param("locationId") Long locationId);

    @Query(value = """
    SELECT ar.id AS request_id,
           ar.description,
           ar.created_at,
           at.id,
           art.quantity
    FROM aid_requests ar
    JOIN aid_request_types art ON ar.id = art.aid_request_id
    JOIN aid_types at ON art.aid_type_id = at.id
    WHERE ar.location_id = :locationId
    ORDER BY ar.id
    """, nativeQuery = true)
    List<Object[]> getAidRequestDetailsByLocationId(@Param("locationId") Long locationId);

    Optional<AidRequest> findByIdAndPerson_Id(Long id, Long userId);







}
