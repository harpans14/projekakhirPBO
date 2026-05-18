package com.example.trashformer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "setoran_sampah")
public class SetoranSampah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warga_id", nullable = false)
    private User warga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kategori_id", nullable = false)
    private KategoriSampah kategori;

    @Column(name = "berat_kg", precision = 10, scale = 2, nullable = false)
    private BigDecimal beratKg;

    @Column(name = "total_harga", precision = 12, scale = 2)
    private BigDecimal totalHarga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSetoran status = StatusSetoran.MENUNGGU;

    private String catatan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petugas_id")
    private User petugas;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public SetoranSampah() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusSetoran.MENUNGGU;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getWarga() {
        return warga;
    }

    public void setWarga(User warga) {
        this.warga = warga;
    }

    public KategoriSampah getKategori() {
        return kategori;
    }

    public void setKategori(KategoriSampah kategori) {
        this.kategori = kategori;
    }

    public BigDecimal getBeratKg() {
        return beratKg;
    }

    public void setBeratKg(BigDecimal beratKg) {
        this.beratKg = beratKg;
    }

    public BigDecimal getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(BigDecimal totalHarga) {
        this.totalHarga = totalHarga;
    }

    public StatusSetoran getStatus() {
        return status;
    }

    public void setStatus(StatusSetoran status) {
        this.status = status;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public User getPetugas() {
        return petugas;
    }

    public void setPetugas(User petugas) {
        this.petugas = petugas;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
