package com.example.trashformer.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.trashformer.model.SetoranSampah;
import com.example.trashformer.model.StatusSetoran;

public interface SetoranSampahRepository extends JpaRepository<SetoranSampah, Long> {

    List<SetoranSampah> findByWargaIdOrderByCreatedAtDesc(Long wargaId);

    List<SetoranSampah> findAllByOrderByCreatedAtDesc();

    List<SetoranSampah> findByStatusOrderByCreatedAtDesc(StatusSetoran status);

    List<SetoranSampah> findByPetugasIdOrderByCreatedAtDesc(Long petugasId);

    long countByStatus(StatusSetoran status);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.beratKg), 0) FROM SetoranSampah s WHERE s.kategori.id = :kategoriId AND s.status = 'DITERIMA'")
    Double sumBeratByKategoriId(@Param("kategoriId") Long kategoriId);

    @Query("SELECT COALESCE(SUM(s.beratKg), 0) FROM SetoranSampah s WHERE s.createdAt BETWEEN :start AND :end")
    Double sumBeratByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.beratKg), 0) FROM SetoranSampah s WHERE s.status = 'DITERIMA'")
    Double sumAllBeratDiterima();

    @Query("SELECT COUNT(s) FROM SetoranSampah s WHERE s.createdAt BETWEEN :start AND :end")
    long countByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
