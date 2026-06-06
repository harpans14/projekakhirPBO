-- =====================================================
-- Seed Data untuk Trashformer
-- Dijalankan otomatis oleh Spring Boot saat DB kosong
-- =====================================================

-- Users (password: admin123 untuk semua seed user)
INSERT IGNORE INTO users (id, nama, username, password, role, created_at, updated_at, is_active)
VALUES
(1, 'Administrator', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', NOW(), NOW(), true),
(2, 'Petugas Sampah', 'petugas', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PETUGAS', NOW(), NOW(), true),
(3, 'Warga', 'warga', '$2a$10$C/zMmLRMy.hva282Zyg7O.GPZEY9mrzk1N8lzjITvAbOr84CEzfl2', 'WARGA', NOW(), NOW(), true);

-- Kategori Sampah
INSERT IGNORE INTO kategori_sampah (id, created_at, deskripsi, harga_per_kg, nama)
VALUES
(1, NOW(), 'Sampah Bahan Berbahaya Beracun', 1500.00, 'B3'),
(2, NOW(), 'Sampah plastik', 1000.00, 'Plastik'),
(3, NOW(), 'Sampah organik', 500.00, 'Organik');
