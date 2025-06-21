package com.yazilim.afet.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class SupportRequestTypeId implements Serializable {

    private Long supportRequestId;
    private Long aidTypeId;

    public SupportRequestTypeId() {}

    public SupportRequestTypeId(Long supportRequestId, Long aidTypeId) {
        this.supportRequestId = supportRequestId;
        this.aidTypeId = aidTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupportRequestTypeId)) return false;
        SupportRequestTypeId that = (SupportRequestTypeId) o;
        return Objects.equals(supportRequestId, that.supportRequestId) &&
                Objects.equals(aidTypeId, that.aidTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supportRequestId, aidTypeId);
    }

}

