package com.yazilim.afet.dto;

import com.yazilim.afet.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RegisterRequestDTO {

    @NotBlank(message = "Ad zorunludur")
    @Size(min = 2, max = 32, message = "Ad en az 2, en fazla 32 karakter olmalıdır")
    @Pattern(regexp = "^[a-zA-ZçÇğĞıİöÖşŞüÜ]+$", message = "Ad sadece harf içerebilir")
    private String name;

    @NotBlank(message = "Soyad zorunludur")
    @Size(min = 2, max = 32, message = "Soyad en az 2, en fazla 32 karakter olmalıdır")
    @Pattern(regexp = "^[a-zA-ZçÇğĞıİöÖşŞüÜ]+$", message = "Soyad sadece harf içerebilir")
    private String surname;

    @NotBlank(message = "TC kimlik numarası zorunludur")
    @Pattern(regexp = "^\\d{11}$", message = "TC kimlik numarası 11 haneli sayı olmalıdır")
    private String tc;

    @NotBlank(message = "E-posta zorunludur")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    private String email;

    @NotBlank(message = "Telefon numarası zorunludur")
    @Pattern(regexp = "^\\d{10,16}$", message = "Telefon numarası 10-16 haneli olmalıdır")
    private String phoneNumber;

    @NotBlank(message = "Şifre zorunludur")
    @Size(min = 8, max = 32, message = "Şifre en az 8, en fazla 32 karakter olmalıdır")
    private String password;

    @NotNull(message = "STK üyeliği bilgisi zorunludur")
    private Boolean stkMember;


    private String stkName;

}
