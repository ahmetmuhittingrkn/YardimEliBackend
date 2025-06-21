package com.yazilim.afet.entity;

import com.yazilim.afet.entity.id.SupportRequestTypeId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "support_request_types")
@NoArgsConstructor
public class SupportRequestType {

    @EmbeddedId
    private SupportRequestTypeId id = new SupportRequestTypeId();

    @ManyToOne
    @MapsId("supportRequestId")
    @JoinColumn(name = "support_request_id")
    private SupportRequest supportRequest;

    @ManyToOne
    @MapsId("aidTypeId")
    @JoinColumn(name = "aid_type_id")
    private AidType aidType;

    private Integer quantity;
}

