package com.yazilim.afet.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class AidRequestTypeId implements Serializable {

    private Long aidRequestId;
    private Long aidTypeId;

    public AidRequestTypeId() {}

    public AidRequestTypeId(Long aidRequestId, Long aidTypeId) {
        this.aidRequestId = aidRequestId;
        this.aidTypeId = aidTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AidRequestTypeId)) return false;
        AidRequestTypeId that = (AidRequestTypeId) o;
        return Objects.equals(aidRequestId, that.aidRequestId) &&
                Objects.equals(aidTypeId, that.aidTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aidRequestId, aidTypeId);
    }

}
