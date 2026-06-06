# 4 Pilar OOP — Trashformer

## 1. Encapsulation (Enkapsulasi)

Menyembunyikan state internal dan hanya mengekspos melalui method publik.

**Private fields + public getter/setter** — `User.java:23-52`
```java
private Long id;
private String nama;
private String password;
private BigDecimal saldo = BigDecimal.ZERO;

public Long getId() { return id; }
public void setNama(String nama) { this.nama = nama; }
```

**Lifecycle callback internal** — `Setoran.java:89-103`
```java
@PrePersist
protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if (this.jenisSetoran == JenisSetoran.SAMPAH) {
        if (this.status == null) this.status = StatusSetoran.MENUNGGU;
    }
}
```
`createdAt` & `updatedAt` tidak punya public setter — class menjaga integritasnya sendiri.

**Private final dependency injection** — `BankSampahService.java:24-37`
```java
private final UserRepository userRepository;
private final SaldoTransaksiRepository saldoTransaksiRepository;
// ...
```
Dependensi di-inject via constructor, tidak bisa diakses dari luar.

---

## 2. Inheritance (Pewarisan)

Class/interface mewarisi kontrak atau perilaku dari parent type.

**Implement interface** — `CustomUserDetailsService.java:12`
```java
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) { ... }
}
```

**Extend interface framework** — `WebConfig.java:12`
```java
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { ... }
}
```

**6 repository extends `JpaRepository<T, ID>`** — `UserRepository.java:11`
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
}
```
Mewarisi `save()`, `findById()`, `findAll()`, `deleteById()`, dll.

---

## 3. Polymorphism (Polimorfisme)

Objek dari tipe berbeda diperlakukan secara seragam melalui interface bersama; method yang tepat di-resolve di runtime.

**Runtime via `PasswordEncoder`** — `SecurityConfig.java:96-97` + `UserService.java:17`
```java
// SecurityConfig — bean mengembalikan interface PasswordEncoder
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// UserService — dikode ke interface, bukan konkrit
private final PasswordEncoder passwordEncoder;
user.setPassword(passwordEncoder.encode(password.trim()));
```

**Runtime via `AuthenticationSuccessHandler` (lambda)** — `SecurityConfig.java:76-93`
```java
@Bean
public AuthenticationSuccessHandler successHandler() {
    return (request, response, authentication) -> {
        // redirect berbeda tergantung role
    };
}
```

**Runtime via `UserDetailsService`** — Spring Security memanggil `loadUserByUsername()` pada object `CustomUserDetailsService` melalui interface `UserDetailsService`.

---

## 4. Abstraction (Abstraksi)

Menyembunyikan kompleksitas implementasi di balik interface/signature yang sederhana.

**Repository interface abstrak SQL/JDBC** — `SetoranRepository.java:16-57`
```java
public interface SetoranRepository extends JpaRepository<Setoran, Long> {
    List<Setoran> findByWargaIdOrderByCreatedAtDesc(Long wargaId);
    
    @Query("SELECT k.nama, COALESCE(SUM(s.beratKg), 0) FROM Setoran s "
         + "JOIN s.kategori k WHERE s.status = 'DITERIMA' GROUP BY k.nama")
    List<Object[]> sumBeratPerKategori();
}
```
Caller tidak tahu (dan tidak peduli) SQL apa yang di-generate.

**Service method abstrak multi-step logic** — `BankSampahService.java:40-57`
```java
@Transactional
public SaldoTransaksi kreditSaldo(Long userId, BigDecimal jumlah,
                                   String keterangan, Long setoranId) {
    // 6 langkah: fetch user, validasi, hitung saldo, update, audit trail, persist
}
```
Controller cukup panggil `bankSampahService.kreditSaldo(...)` — detail transaksional & audit trail disembunyikan.

**JPA annotations abstrak ORM** — `User.java:17-52`
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nama;
    @Enumerated(EnumType.STRING)
    private Role role;
}
```
Developer tidak menulis SQL CRUD — JPA/Hibernate menanganinya.
