package com.yazilim.afet.util;

import com.yazilim.afet.entity.Person;
import com.yazilim.afet.entity.SupportRequest;
import com.yazilim.afet.enums.Role;
import com.yazilim.afet.enums.SupportStatus;
import com.yazilim.afet.exception.ForbiddenException;
import com.yazilim.afet.exception.UnauthorizedException;

public class BusinessValidationUtil {

    public static void validateStkVolunteerAccess(Person person, String operation) {
        if (person.getRole() != Role.STK_GONULLUSU || !person.getIsActive()) {
            throw new ForbiddenException("Sadece aktif STK gönüllüleri " + operation + " yapabilir.");
        }
    }

    public static void validateRequestOwnership(SupportRequest supportRequest, Long personId, String operation) {
        if (!supportRequest.getPerson().getId().equals(personId)) {
            throw new UnauthorizedException("Sadece talep sahibi " + operation + " yapabilir.");
        }
    }

    public static void validateSupportRequestStatus(SupportRequest supportRequest, SupportStatus expectedStatus, String operation) {
        if (supportRequest.getStatus() != expectedStatus) {
            throw new ForbiddenException("Durum '" + expectedStatus.name() + "' olmalıdır.");
        }
    }

    public static void validatePendingStatus(SupportRequest supportRequest, String operation) {
        if (supportRequest.getStatus() != SupportStatus.BEKLEMEDE) {
            throw new ForbiddenException("Yalnızca 'BEKLEMEDE' durumundaki talepler " + operation + " yapılabilir.");
        }
    }
} 