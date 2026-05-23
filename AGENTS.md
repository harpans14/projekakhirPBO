# Trashformer — Bank Sampah App

## Stack
- Java 17, Spring Boot 3.3.5, Maven wrapper (`mvnw.cmd`)
- MySQL + JPA/Hibernate (`ddl-auto=update` — no Flyway/Liquibase)
- Thymeleaf (Layout Dialect), Spring Security (BCrypt), Devtools
- No tests exist (`src/test/` is empty)

## Quick Start
```powershell
# Run (requires MySQL at localhost:3306, schema `trashformer_db`)
.\mvnw.cmd spring-boot:run

# Package
.\mvnw.cmd clean package
```

## Database
- Schema + seed data: `trashformer_db.sql` (import once)
- Migration scripts: `migrasi_setoran_unified.sql`, `fix_user_lawas.sql`
- JPA `ddl-auto=update` creates/updates tables; you must create the schema manually
- Default users (password from seed): admin (ADMIN), petugas (PETUGAS), warga (WARGA)

## Project Structure
```
src/main/java/com/example/trashformer/
  config/   — SecurityConfig, WebConfig
  controller/ — AuthController, DashboardController, AdminController, PetugasController, WargaController
  model/    — User, Setoran, KategoriSampah + enums (Role, StatusSetoran, StatusPembayaran, etc.)
  repository/ — JPA repos
  service/  — UserService, SetoranService, CustomUserDetailsService
```

## Key Conventions
- All views are Thymeleaf templates in `src/main/resources/templates/` (admin/, petugas/, warga/)
- Layout via `layout.html` using Thymeleaf Layout Dialect (not fragments)
- Uploaded payment proofs → `uploads/bukti_pembayaran/`, served at `/files/bukti_pembayaran/**`
- Session: 30min timeout, cookie-based, single-session per user
- File uploads: max 5MB per file, 10MB per request
- Role-based routing: `/admin/**`, `/petugas/**`, `/warga/**` — each controller uses `@RequestMapping`
- Static resources at `/css/**`, `/js/**` in `src/main/resources/static/`

## Routes Summary
| Path | Role | Purpose |
|------|------|---------|
| `/login`, `/register` | public | Auth |
| `/admin/**` | ADMIN | Users CRUD, categories, all setoran |
| `/petugas/**` | PETUGAS | Create/verify setoran, manage pickups |
| `/warga/**` | WARGA | Submit setoran, profile, history |

## Notable Quirks
- Setoran table is **unified** (both waste deposits & money deposits via `jenis_setoran` enum)
- Payment verification flow: warga uploads proof → petugas verifies → pickup scheduled
- Admin reset password defaults to `"123456"`
- Devtools live-reload active (runtime scope)
