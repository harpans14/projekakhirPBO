-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 18, 2026 at 03:12 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `trashformer_db`
--
CREATE DATABASE IF NOT EXISTS `trashformer_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `trashformer_db`;

-- --------------------------------------------------------

--
-- Table structure for table `kategori_sampah`
--

CREATE TABLE `kategori_sampah` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deskripsi` varchar(255) DEFAULT NULL,
  `harga_per_kg` decimal(10,2) DEFAULT NULL,
  `nama` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `setoran`
--

CREATE TABLE `setoran` (
  `id` bigint(20) NOT NULL,
  `jenis_setoran` enum('SAMPAH','UANG') NOT NULL,
  `warga_id` bigint(20) NOT NULL,
  `petugas_id` bigint(20) DEFAULT NULL,
  `kategori_id` bigint(20) DEFAULT NULL,
  `berat_kg` decimal(10,2) DEFAULT NULL,
  `total_harga` decimal(12,2) DEFAULT NULL,
  `status` enum('MENUNGGU','DITERIMA','DITOLAK') DEFAULT NULL,
  `alamat_jemput` varchar(255) DEFAULT NULL,
  `bukti_pembayaran` varchar(255) DEFAULT NULL,
  `status_pembayaran` enum('MENUNGGU_VERIFIKASI','DISETUJUI','DITOLAK') DEFAULT NULL,
  `status_penjemputan` enum('DIJADWALKAN','SEDANG_DIJEMPUT','SELESAI') DEFAULT NULL,
  `jumlah_uang` decimal(15,2) DEFAULT NULL,
  `jenis_uang` varchar(255) DEFAULT NULL,
  `deskripsi` varchar(255) DEFAULT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `jenis_sampah` enum('ORGANIK','ANORGANIK','B3') DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','PETUGAS','WARGA') NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `no_telepon` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nama`, `username`, `password`, `role`, `created_at`, `updated_at`, `alamat`, `is_active`, `no_telepon`) VALUES
(1, 'Administrator', 'admin', '$2a$10$C/zMmLRMy.hva282Zyg7O.GPZEY9mrzk1N8lzjITvAbOr84CEzfl2', 'ADMIN', NULL, NULL, NULL, NULL, NULL),
(2, 'Petugas Sampah', 'petugas', '$2a$10$C/zMmLRMy.hva282Zyg7O.GPZEY9mrzk1N8lzjITvAbOr84CEzfl2', 'PETUGAS', NULL, NULL, NULL, NULL, NULL),
(3, 'Budi Warga', 'warga', '$2a$10$C/zMmLRMy.hva282Zyg7O.GPZEY9mrzk1N8lzjITvAbOr84CEzfl2', 'WARGA', NULL, NULL, NULL, NULL, NULL),
(4, 'harpan', 'harpan', '$2a$10$wOhX0MQXSx30U5RqqHydy.r7bACY0dB5hKyWDYNaEl8OMvN9jC8Wy', 'WARGA', '2026-05-18 07:35:45.000000', '2026-05-18 07:35:45.000000', NULL, NULL, NULL),
(5, 'harpan', 'harpan12', '$2a$10$32R5UVeI2uRA.eG0KlrN9eX2KQsT7ASrwyG1.O05wW51EnNqPldvq', 'WARGA', '2026-05-18 07:54:28.000000', '2026-05-18 07:54:28.000000', NULL, b'1', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kategori_sampah`
--
ALTER TABLE `kategori_sampah`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK80j8qyluygllwqvnbiin6fsyu` (`nama`);

--
-- Indexes for table `setoran`
--
ALTER TABLE `setoran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_warga` (`warga_id`),
  ADD KEY `fk_petugas` (`petugas_id`),
  ADD KEY `fk_kategori` (`kategori_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kategori_sampah`
--
ALTER TABLE `kategori_sampah`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `setoran`
--
ALTER TABLE `setoran`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `setoran`
--
ALTER TABLE `setoran`
  ADD CONSTRAINT `fk_setoran_warga` FOREIGN KEY (`warga_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_setoran_petugas` FOREIGN KEY (`petugas_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_setoran_kategori` FOREIGN KEY (`kategori_id`) REFERENCES `kategori_sampah` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
