# 🚨 Afet Yardım Sistemi Backend

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13+-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Proje Hakkında

Afet Yardım Sistemi, doğal afetler sırasında yardım taleplerini yönetmek ve STK gönüllülerinin destek sağlamasını kolaylaştırmak amacıyla geliştirilmiş bir Spring Boot backend uygulamasıdır. Sistem, afetzedelerin yardım taleplerini oluşturmasına ve STK gönüllülerinin bu talepleri yönetmesine olanak sağlar.

## ✨ Özellikler

### 🔐 Kimlik Doğrulama ve Yetkilendirme
- **Kullanıcı Kaydı**: E-posta doğrulaması ile güvenli kayıt sistemi
- **Giriş Sistemi**: JWT tabanlı kimlik doğrulama
- **Rol Tabanlı Yetkilendirme**: USER ve STK_GONULLUSU rolleri
- **E-posta Doğrulama**: Kayıt sonrası otomatik e-posta doğrulama

### 🆘 Yardım Talepleri Yönetimi
- **Yardım Talebi Oluşturma**: Afetzedelerin ihtiyaçlarını belirtmesi
- **Talep Kategorileri**: Farklı yardım türleri (gıda, ilaç, barınma vb.)
- **Konum Bazlı Filtreleme**: Bölgesel yardım talepleri
- **Talep Silme**: Kullanıcıların kendi taleplerini silmesi

### 🤝 Destek Talepleri Yönetimi
- **Destek Talebi Oluşturma**: STK gönüllülerinin yardım sağlama talepleri
- **Durum Takibi**: BEKLEMEDE → ONAYLANDI → YOLDA → VARDI
- **Onay Sistemi**: STK gönüllülerinin talepleri onaylaması
- **Gerçek Zamanlı Güncelleme**: Talep durumlarının anlık takibi

### 📊 Raporlama ve Analiz
- **Özet Raporları**: Konum bazlı yardım ve destek özetleri
- **Detaylı Raporlar**: Talep detayları ve istatistikler
- **Bekleyen Talepler**: Onay bekleyen destek talepleri listesi

## 🛠️ Teknolojiler

- **Java 17**: Modern Java özellikleri
- **Spring Boot 3.4.4**: Hızlı geliştirme ve deployment
- **Spring Security**: Güvenlik ve kimlik doğrulama
- **Spring Data JPA**: Veritabanı işlemleri
- **PostgreSQL**: İlişkisel veritabanı
- **Lombok**: Kod tekrarını azaltma
- **Maven**: Bağımlılık yönetimi
- **Spring Mail**: E-posta gönderimi

## 📁 Proje Yapısı

```
src/main/java/com/yazilim/afet/
├── config/                 # Konfigürasyon sınıfları
├── controller/            # REST API kontrolcüleri
├── dto/                  # Veri transfer nesneleri
├── entity/               # JPA varlık sınıfları
├── enums/                # Enum sınıfları
├── exception/            # Özel istisna sınıfları
├── mapper/               # DTO-Entity dönüştürücüleri
├── repository/           # Veritabanı erişim katmanı
├── service/              # İş mantığı katmanı
│   └── impl/            # Servis implementasyonları
├── util/                 # Yardımcı sınıflar
└── validation/           # Doğrulama sınıfları
```

## 🚀 Kurulum ve Çalıştırma

### Gereksinimler
- Java 17 veya üzeri
- Maven 3.6+
- PostgreSQL 13+
- Git

### Adım 1: Projeyi Klonlayın
```bash
git clone https://github.com/ahmetmuhittingrkn/YardimEliBackend.git
cd YardımEliBackend
```

### Adım 2: Veritabanını Hazırlayın
```sql
-- PostgreSQL'de yeni veritabanı oluşturun
CREATE DATABASE afet;
```

### Adım 3: Konfigürasyonu Ayarlayın
`src/main/resources/application.properties` dosyasını düzenleyin:

```properties
# Veritabanı ayarları
spring.datasource.url=jdbc:postgresql://localhost:5432/afet
spring.datasource.username=your_username
spring.datasource.password=your_password

# E-posta ayarları (Gmail için)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

### Adım 4: Uygulamayı Çalıştırın
```bash
# Maven ile derleme ve çalıştırma
mvn spring-boot:run

# Veya JAR dosyası oluşturup çalıştırma
mvn clean package
java -jar target/afet-0.0.1-SNAPSHOT.jar
```

Uygulama varsayılan olarak `http://localhost:8080` adresinde çalışacaktır.

## 📚 API Dokümantasyonu

### Kimlik Doğrulama Endpoints

#### Kullanıcı Kaydı
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "Ahmet",
  "surname": "Muhittin",
  "tc": "12345678901",
  "email": "ahmet@example.com",
  "phoneNumber": "5551234567",
  "password": "güvenli123",
  "stkMember": false
}
```

#### Giriş
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "ahmet@example.com",
  "password": "güvenli123"
}
```

#### E-posta Doğrulama
```http
GET /api/auth/confirm?token=verification_token
```

### Afetzede Talepleri Endpoints

#### Afetzede Talebi Oluşturma
```http
POST /api/aid-requests?personId=1
Content-Type: application/json

{
  "description": "Acil gıda ihtiyacı",
  "locationId": 1,
  "aidItems": [
    {
      "aidTypeId": 1,
      "quantity": 5
    }
  ]
}
```

#### Konum Bazlı Afetzede Talepleri Özeti
```http
GET /api/aid-requests/summary?locationId=1
```

#### Konum Bazlı Afetzede Talebi Detayları
```http
GET /api/aid-requests/location/1
```

### Destek Talepleri Endpoints

#### Destek Talebi Oluşturma
```http
POST /api/support-requests
Content-Type: application/json

{
  "description": "Gıda yardımı sağlayabilirim",
  "locationId": 1,
  "originCity": "İstanbul",
  "arrivalTimeMinutes": 120,
  "supportItems": [
    {
      "aidTypeId": 1,
      "quantity": 10
    }
  ]
}
```

#### Destek Talebini Onaylama
```http
PUT /api/support-requests/1/approve?personId=2
```

#### Destek Durumunu Güncelleme
```http
PUT /api/support-requests/1/start?personId=1
PUT /api/support-requests/1/arrived?personId=1
```

#### Bekleyen Destek Talepleri
```http
GET /api/support-requests/pending
```

## 🔒 Güvenlik

- **Şifre Hashleme**: BCrypt ile güvenli şifre saklama
- **E-posta Doğrulama**: Kayıt sonrası zorunlu e-posta doğrulama
- **Rol Tabanlı Erişim**: Farklı kullanıcı rolleri için yetkilendirme
- **Input Validation**: Tüm giriş verilerinin doğrulanması
- **Exception Handling**: Güvenli hata yönetimi


## 📝 Katkıda Bulunma

1. Bu repository'yi fork edin
2. Yeni bir feature branch oluşturun (`git checkout -b feature/yeni-ozellik`)
3. Değişikliklerinizi commit edin (`git commit -am 'Yeni özellik eklendi'`)
4. Branch'inizi push edin (`git push origin feature/yeni-ozellik`)
5. Pull Request oluşturun

## 📞 İletişim

- **E-posta**: a.muhittin.grkn@gmail.com
- **GitHub**: [@ahmetmuhittingrkn](https://github.com/ahmetmuhittingrkn)
- **LinkedIn**: [Ahmet Muhittin Gürkan](https://www.linkedin.com/in/ahmet-muhittin-gürkan-52aab1219/)

## 🙏 Teşekkürler

Bu proje, afet durumlarında yardıma ihtiyaç duyan insanlara destek olmak amacıyla geliştirilmiştir. Tüm katkıda bulunanlara teşekkürler!

---

⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın! 