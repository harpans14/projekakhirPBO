package com.example.trashformer.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.trashformer.model.KategoriSampah;
import com.example.trashformer.model.SetoranSampah;
import com.example.trashformer.model.SetoranUang;
import com.example.trashformer.model.StatusSetoran;
import com.example.trashformer.model.User;
import com.example.trashformer.repository.KategoriSampahRepository;
import com.example.trashformer.repository.SetoranSampahRepository;
import com.example.trashformer.repository.SetoranUangRepository;
import com.example.trashformer.repository.UserRepository;

@Service
public class SetoranService {

    private final SetoranSampahRepository setoranSampahRepository;
    private final SetoranUangRepository setoranUangRepository;
    private final KategoriSampahRepository kategoriSampahRepository;
    private final UserRepository userRepository;

    public SetoranService(SetoranSampahRepository setoranSampahRepository,
                          SetoranUangRepository setoranUangRepository,
                          KategoriSampahRepository kategoriSampahRepository,
                          UserRepository userRepository) {
        this.setoranSampahRepository = setoranSampahRepository;
        this.setoranUangRepository = setoranUangRepository;
        this.kategoriSampahRepository = kategoriSampahRepository;
        this.userRepository = userRepository;
    }

    public SetoranSampah createSetoranSampah(Long wargaId, Long kategoriId, BigDecimal beratKg, String catatan) {
        User warga = userRepository.findById(wargaId)
                .orElseThrow(() -> new RuntimeException("Warga tidak ditemukan"));
        KategoriSampah kategori = kategoriSampahRepository.findById(kategoriId)
                .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));

        SetoranSampah setoran = new SetoranSampah();
        setoran.setWarga(warga);
        setoran.setKategori(kategori);
        setoran.setBeratKg(beratKg);

        BigDecimal hargaPerKg = kategori.getHargaPerKg();
        if (hargaPerKg != null) {
            setoran.setTotalHarga(beratKg.multiply(hargaPerKg));
        }

        if (catatan != null && !catatan.trim().isEmpty()) {
            setoran.setCatatan(catatan.trim());
        }

        return setoranSampahRepository.save(setoran);
    }

    public SetoranSampah verifikasiSetoran(Long setoranId, Long petugasId, StatusSetoran status, String catatan) {
        SetoranSampah setoran = setoranSampahRepository.findById(setoranId)
                .orElseThrow(() -> new RuntimeException("Setoran tidak ditemukan"));
        User petugas = userRepository.findById(petugasId)
                .orElseThrow(() -> new RuntimeException("Petugas tidak ditemukan"));

        setoran.setStatus(status);
        setoran.setPetugas(petugas);
        if (catatan != null && !catatan.trim().isEmpty()) {
            setoran.setCatatan(catatan.trim());
        }

        return setoranSampahRepository.save(setoran);
    }

    public List<SetoranSampah> getAllSetoranSampah() {
        return setoranSampahRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<SetoranSampah> getSetoranByWarga(Long wargaId) {
        return setoranSampahRepository.findByWargaIdOrderByCreatedAtDesc(wargaId);
    }

    public List<SetoranSampah> getSetoranByStatus(StatusSetoran status) {
        return setoranSampahRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public List<SetoranSampah> getSetoranByPetugas(Long petugasId) {
        return setoranSampahRepository.findByPetugasIdOrderByCreatedAtDesc(petugasId);
    }

    public SetoranUang createSetoranUang(Long wargaId, BigDecimal jumlah, String jenis, String deskripsi, Long petugasId) {
        User warga = userRepository.findById(wargaId)
                .orElseThrow(() -> new RuntimeException("Warga tidak ditemukan"));

        SetoranUang setoran = new SetoranUang();
        setoran.setWarga(warga);
        setoran.setJumlah(jumlah);
        setoran.setJenis(jenis);
        if (deskripsi != null && !deskripsi.trim().isEmpty()) {
            setoran.setDeskripsi(deskripsi.trim());
        }

        if (petugasId != null) {
            User petugas = userRepository.findById(petugasId)
                    .orElseThrow(() -> new RuntimeException("Petugas tidak ditemukan"));
            setoran.setPetugas(petugas);
        }

        return setoranUangRepository.save(setoran);
    }

    public List<SetoranUang> getAllSetoranUang() {
        return setoranUangRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<SetoranUang> getSetoranUangByWarga(Long wargaId) {
        return setoranUangRepository.findByWargaIdOrderByCreatedAtDesc(wargaId);
    }

    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();

        long totalWarga = userRepository.countByRole(com.example.trashformer.model.Role.WARGA);
        long totalPetugas = userRepository.countByRole(com.example.trashformer.model.Role.PETUGAS);

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        long totalSetoranHariIni = setoranSampahRepository.countByCreatedAtBetween(todayStart, todayEnd);

        long totalMenunggu = setoranSampahRepository.countByStatus(StatusSetoran.MENUNGGU);
        long totalDiterima = setoranSampahRepository.countByStatus(StatusSetoran.DITERIMA);
        long totalDitolak = setoranSampahRepository.countByStatus(StatusSetoran.DITOLAK);

        Double totalBerat = setoranSampahRepository.sumAllBeratDiterima();
        if (totalBerat == null) totalBerat = 0.0;

        Double totalSetoranUang = setoranUangRepository.sumJumlahByJenis("SETORAN");
        if (totalSetoranUang == null) totalSetoranUang = 0.0;

        stats.put("totalWarga", totalWarga);
        stats.put("totalPetugas", totalPetugas);
        stats.put("totalSetoranHariIni", totalSetoranHariIni);
        stats.put("totalMenunggu", totalMenunggu);
        stats.put("totalDiterima", totalDiterima);
        stats.put("totalDitolak", totalDitolak);
        stats.put("totalBerat", totalBerat.longValue());
        stats.put("totalSetoranUang", totalSetoranUang.longValue());

        return stats;
    }
}
