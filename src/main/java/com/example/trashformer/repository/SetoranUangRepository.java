package com.example.trashformer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.trashformer.model.SetoranUang;

public interface SetoranUangRepository extends JpaRepository<SetoranUang, Long> {

    List<SetoranUang> findByWargaIdOrderByCreatedAtDesc(Long wargaId);

    List<SetoranUang> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COALESCE(SUM(s.jumlah), 0) FROM SetoranUang s WHERE s.jenis = :jenis")
    Double sumJumlahByJenis(@Param("jenis") String jenis);
}
