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
  `user_id` bigint(20) NOT NULL,
  `jenis_setoran` enum('SAMPAH','UANG') NOT NULL,
  `jenis_sampah` enum('ORGANIK','B3','ANORGANIK') DEFAULT NULL,
  `berat_kg` double DEFAULT NULL,
  `jumlah_uang` decimal(15,2) DEFAULT NULL,
  `tanggal` datetime DEFAULT current_timestamp(),
  `keterangan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `setoran`
--

INSERT INTO `setoran` (`id`, `user_id`, `jenis_setoran`, `jenis_sampah`, `berat_kg`, `jumlah_uang`, `tanggal`, `keterangan`) VALUES
(1, 3, 'SAMPAH', 'ORGANIK', 5.5, NULL, '2026-05-18 07:24:10', 'Setoran sampah organik'),
(2, 3, 'UANG', NULL, NULL, 50000.00, '2026-05-18 07:24:10', 'Iuran kebersihan');

-- --------------------------------------------------------

--
-- Table structure for table `setoran_sampah`
--

CREATE TABLE `setoran_sampah` (
  `id` bigint(20) NOT NULL,
  `berat_kg` decimal(10,2) NOT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `status` enum('DITERIMA','DITOLAK','MENUNGGU') NOT NULL,
  `total_harga` decimal(12,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `kategori_id` bigint(20) NOT NULL,
  `petugas_id` bigint(20) DEFAULT NULL,
  `warga_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `setoran_uang`
--

CREATE TABLE `setoran_uang` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deskripsi` varchar(255) DEFAULT NULL,
  `jenis` varchar(255) NOT NULL,
  `jumlah` decimal(12,2) NOT NULL,
  `petugas_id` bigint(20) DEFAULT NULL,
  `warga_id` bigint(20) NOT NULL
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
  ADD KEY `fk_user_setoran` (`user_id`);

--
-- Indexes for table `setoran_sampah`
--
ALTER TABLE `setoran_sampah`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5adcr2dvec9dihdewotbbv6eh` (`kategori_id`),
  ADD KEY `FK95rqd395x6b686y4ybkwylshf` (`petugas_id`),
  ADD KEY `FK3kh4dyn61ol28wr5r8llb1xgy` (`warga_id`);

--
-- Indexes for table `setoran_uang`
--
ALTER TABLE `setoran_uang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9katllu10g7l1v0ii5tbjti68` (`petugas_id`),
  ADD KEY `FKq0d2xoq2w3b2wavayhl3rqlwa` (`warga_id`);

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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `setoran_sampah`
--
ALTER TABLE `setoran_sampah`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `setoran_uang`
--
ALTER TABLE `setoran_uang`
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
  ADD CONSTRAINT `fk_user_setoran` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `setoran_sampah`
--
ALTER TABLE `setoran_sampah`
  ADD CONSTRAINT `FK3kh4dyn61ol28wr5r8llb1xgy` FOREIGN KEY (`warga_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FK5adcr2dvec9dihdewotbbv6eh` FOREIGN KEY (`kategori_id`) REFERENCES `kategori_sampah` (`id`),
  ADD CONSTRAINT `FK95rqd395x6b686y4ybkwylshf` FOREIGN KEY (`petugas_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `setoran_uang`
--
ALTER TABLE `setoran_uang`
  ADD CONSTRAINT `FK9katllu10g7l1v0ii5tbjti68` FOREIGN KEY (`petugas_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKq0d2xoq2w3b2wavayhl3rqlwa` FOREIGN KEY (`warga_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
