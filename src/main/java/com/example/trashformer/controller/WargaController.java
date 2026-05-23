package com.example.trashformer.controller;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.trashformer.model.KategoriSampah;
import com.example.trashformer.model.SetoranSampah;
import com.example.trashformer.model.SetoranUang;
import com.example.trashformer.model.User;
import com.example.trashformer.repository.KategoriSampahRepository;
import com.example.trashformer.repository.SetoranSampahRepository;
import com.example.trashformer.repository.SetoranUangRepository;
import com.example.trashformer.repository.UserRepository;
import com.example.trashformer.service.SetoranService;
import com.example.trashformer.service.UserService;

@Controller
@RequestMapping("/warga")
public class WargaController {

    private final UserService userService;
    private final SetoranService setoranService;
    private final SetoranSampahRepository setoranSampahRepository;
    private final SetoranUangRepository setoranUangRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KategoriSampahRepository kategoriSampahRepository;

    @Value("${app.upload.dir:uploads/bukti_pembayaran}")
    private String uploadDir;

    public WargaController(UserService userService,
                           SetoranService setoranService,
                           SetoranSampahRepository setoranSampahRepository,
                           SetoranUangRepository setoranUangRepository,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           KategoriSampahRepository kategoriSampahRepository) {
        this.userService = userService;
        this.setoranService = setoranService;
        this.setoranSampahRepository = setoranSampahRepository;
        this.setoranUangRepository = setoranUangRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.kategoriSampahRepository = kategoriSampahRepository;
    }

    private void addUserToModel(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("nama", user.getNama());
                model.addAttribute("role", user.getRole().name());
            }
        }
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        addUserToModel(authentication, model);
        User warga = getCurrentUser(authentication);
        if (warga != null) {
            Long wargaId = warga.getId();
            List<SetoranSampah> sampahList = setoranSampahRepository.findByWargaIdOrderByCreatedAtDesc(wargaId);
            List<SetoranUang> uangList = setoranUangRepository.findByWargaIdOrderByCreatedAtDesc(wargaId);

            double totalBerat = sampahList.stream()
                    .mapToDouble(s -> s.getBeratKg() != null ? s.getBeratKg().doubleValue() : 0.0)
                    .sum();

            double totalUangDisetor = uangList.stream()
                    .filter(u -> "SETORAN".equals(u.getJenis()))
                    .mapToDouble(u -> u.getJumlah() != null ? u.getJumlah().doubleValue() : 0.0)
                    .sum();

            model.addAttribute("totalSampah", (long) totalBerat);
            model.addAttribute("totalSetoran", sampahList.size());
            model.addAttribute("totalUang", (long) totalUangDisetor);

            List<SetoranSampah> recent = sampahList.size() > 5 ? sampahList.subList(0, 5) : sampahList;
            model.addAttribute("recentSetoran", recent);
        }
        return "warga/dashboard";
    }

    @GetMapping("/profil")
    public String profil(Authentication authentication, Model model) {
        addUserToModel(authentication, model);
        User user = getCurrentUser(authentication);
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "warga/profil";
    }

    @PostMapping("/profil/update")
    public String updateProfil(@RequestParam String nama,
                                @RequestParam(required = false) String alamat,
                                @RequestParam(required = false) String noTelepon,
                                Authentication authentication,
                                RedirectAttributes redirectAttrs) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null) {
                redirectAttrs.addFlashAttribute("error", "User tidak ditemukan");
                return "redirect:/warga/profil";
            }
            user.setNama(nama.trim());
            if (alamat != null) user.setAlamat(alamat.trim());
            if (noTelepon != null) user.setNoTelepon(noTelepon.trim());
            userRepository.save(user);
            redirectAttrs.addFlashAttribute("success", "Profil berhasil diupdate");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Gagal mengupdate profil");
        }
        return "redirect:/warga/profil";
    }

    @PostMapping("/profil/ganti-password")
    public String gantiPassword(@RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Authentication authentication,
                                RedirectAttributes redirectAttrs) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            redirectAttrs.addFlashAttribute("error", "User tidak ditemukan");
            return "redirect:/warga/profil";
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttrs.addFlashAttribute("error", "Password saat ini salah");
            return "redirect:/warga/profil";
        }

        if (newPassword.trim().length() < 6) {
            redirectAttrs.addFlashAttribute("error", "Password baru minimal 6 karakter");
            return "redirect:/warga/profil";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttrs.addFlashAttribute("error", "Konfirmasi password tidak cocok");
            return "redirect:/warga/profil";
        }

        try {
            userService.resetPassword(user.getId(), newPassword.trim());
            redirectAttrs.addFlashAttribute("success", "Password berhasil diganti");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Gagal mengganti password");
        }
        return "redirect:/warga/profil";
    }

    @GetMapping("/riwayat")
    public String riwayatSampah(Authentication authentication, Model model) {
        addUserToModel(authentication, model);
        User warga = getCurrentUser(authentication);
        if (warga != null) {
            model.addAttribute("setoranList", setoranService.getSetoranByWarga(warga.getId()));
            model.addAttribute("setoranUangList", setoranService.getSetoranUangByWarga(warga.getId()));
        }
        return "warga/riwayat";
    }

    @GetMapping("/laporan")
    public String laporanForm(Authentication authentication, Model model) {
        addUserToModel(authentication, model);
        User warga = getCurrentUser(authentication);
        if (warga != null) {
            model.addAttribute("alamat", warga.getAlamat() != null ? warga.getAlamat() : "");
        }
        List<KategoriSampah> kategoriList = kategoriSampahRepository.findAll();
        model.addAttribute("listKategori", kategoriList);
        return "warga/laporan";
    }

    @PostMapping("/laporan/simpan")
    public String laporanSimpan(@RequestParam Long kategoriId,
                                @RequestParam BigDecimal beratKg,
                                @RequestParam(required = false) String alamatJemput,
                                @RequestParam(required = false) String catatanTambahan,
                                @RequestParam(value = "buktiPembayaran", required = false) MultipartFile buktiPembayaran,
                                Authentication authentication,
                                RedirectAttributes redirectAttrs) {
        try {
            User warga = getCurrentUser(authentication);
            if (warga == null) {
                redirectAttrs.addFlashAttribute("error", "User tidak ditemukan");
                return "redirect:/warga/laporan";
            }

            String fileName = null;
            if (buktiPembayaran != null && !buktiPembayaran.isEmpty()) {
                String originalName = buktiPembayaran.getOriginalFilename();
                if (originalName != null && !originalName.isEmpty()) {
                    int dotIndex = originalName.lastIndexOf('.');
                    if (dotIndex <= 0) {
                        redirectAttrs.addFlashAttribute("error", "File bukti pembayaran tidak memiliki ekstensi");
                        return "redirect:/warga/laporan";
                    }
                    String extension = originalName.substring(dotIndex).toLowerCase();
                    if (!".jpg.jpeg.png.gif.bmp".contains(extension)) {
                        redirectAttrs.addFlashAttribute("error", "Format file tidak didukung. Gunakan JPG, PNG, GIF, atau BMP");
                        return "redirect:/warga/laporan";
                    }
                    fileName = UUID.randomUUID().toString() + extension;
                    Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
                    Files.createDirectories(uploadPath);
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(buktiPembayaran.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            if (fileName == null) {
                redirectAttrs.addFlashAttribute("error", "Bukti pembayaran wajib diunggah");
                return "redirect:/warga/laporan";
            }

            setoranService.createSetoranSampahWarga(
                    warga.getId(),
                    kategoriId,
                    beratKg,
                    alamatJemput,
                    catatanTambahan,
                    fileName
            );

            redirectAttrs.addFlashAttribute("success", "Laporan setoran berhasil dikirim, menunggu verifikasi pembayaran oleh petugas");
            return "redirect:/warga/dashboard";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Gagal mengirim laporan: " + e.getMessage());
            return "redirect:/warga/laporan";
        }
    }
}
