package tw.edu.fju.miniclinic.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatsController {

    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;

    public StatsController(DoctorRepository doctorRepo, PatientRepository patientRepo, AppointmentRepository appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    // ==========================================
    // [ ] 新增頁面 GET /stats：顯示基本統計資訊
    // ==========================================
    @GetMapping("/stats")
    public String showStats(Model model) {
        // 統計總數
        model.addAttribute("doctorCount", doctorRepo.count());
        model.addAttribute("patientCount", patientRepo.count());
        model.addAttribute("appointmentCount", appointmentRepo.count());

        // 依科別分組，列出每科的掛號數 (直接使用講義中出現過的常見科別)
        List<String> departments = Arrays.asList("家醫科", "內科", "復健科", "小兒科", "身心科");
        Map<String, Long> deptStats = new LinkedHashMap<>();
        
        for (String dept : departments) {
            long count = appointmentRepo.countByDoctor_Department(dept);
            deptStats.put(dept, count);
        }
        model.addAttribute("deptStats", deptStats);

        return "stats"; // 對應 templates/stats.html
    }

    // ==========================================
    // [ ] 新增 API GET /api/appointments/count
    // ==========================================
    @GetMapping("/api/appointments/count")
    @ResponseBody
    public long getAppointmentCount() {
        return appointmentRepo.count();
    }

    // ==========================================
    // [ ] 新增 API GET /api/appointments (支援 date 與 doctorId 篩選)
    // ==========================================
    @GetMapping("/api/appointments")
    @ResponseBody
    public List<Appointment> getAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String doctorId) {

        // 條件 1：若有帶 ?date=YYYY-MM-DD
        if (date != null) {
            return appointmentRepo.findByApptDate(date);
        }

        // 條件 2：若有帶 ?doctorId=D001
        if (doctorId != null && !doctorId.isBlank()) {
            // 先去 DoctorRepository 找該醫師，找不到就回傳空清單
            Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
            if (doctor != null) {
                return appointmentRepo.findByDoctor(doctor);
            }
            return List.of(); // 使用更明確的空清單回傳方式
        }

        // 條件 3：沒帶參數，回傳全部
        return appointmentRepo.findAll();
    }
}