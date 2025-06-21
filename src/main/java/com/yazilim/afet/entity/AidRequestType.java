package com.yazilim.afet.entity;

import com.yazilim.afet.entity.id.AidRequestTypeId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "aid_request_types")
public class AidRequestType {

    @EmbeddedId
    private AidRequestTypeId id = new AidRequestTypeId();

    @ManyToOne
    @MapsId("aidRequestId")
    @JoinColumn(name = "aid_request_id")
    private AidRequest aidRequest;

    @ManyToOne
    @MapsId("aidTypeId")
    @JoinColumn(name = "aid_type_id")
    private AidType aidType;

    private Integer quantity;
}
