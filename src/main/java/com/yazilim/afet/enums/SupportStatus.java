package com.yazilim.afet.enums;

public enum SupportStatus {
    BEKLEMEDE,   // Gönüllü talebi oluşturdu ama STK henüz onaylamadı
    ONAYLANDI,   // STK gönüllüyü onayladı
    YOLDA,       // Gönüllü yola çıktı
    VARDI        // Gönüllü belirtilen konuma ulaştı
}
