package tw.edu.fju.miniclinic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.edu.fju.miniclinic.model.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 提供系統統計摘要的 API，供外部驗收工具查核。
 */
@RestController
public class StatsController {

    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;

    public StatsController(DoctorRepository doctorRepo, PatientRepository patientRepo, AppointmentRepository appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @GetMapping("/api/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        // 使用 LinkedHashMap 保持 JSON 欄位順序
        Map<String, Object> response = new LinkedHashMap<>();
        
        // 取得總量統計
        response.put("totalDoctors", (int) doctorRepo.count());
        response.put("totalPatients", (int) patientRepo.count());
        response.put("totalAppointments", (int) appointmentRepo.count());

        // 取得各狀態的掛號數量
        Map<String, Long> byStatus = new LinkedHashMap<>();
        byStatus.put("BOOKED", appointmentRepo.countByStatus("BOOKED"));
        byStatus.put("COMPLETED", appointmentRepo.countByStatus("COMPLETED"));
        byStatus.put("CANCELLED", appointmentRepo.countByStatus("CANCELLED"));
        
        response.put("byStatus", byStatus);

        return ResponseEntity.ok(response);
    }
}