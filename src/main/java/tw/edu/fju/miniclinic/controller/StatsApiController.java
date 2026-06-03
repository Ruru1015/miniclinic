package tw.edu.fju.miniclinic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class StatsApiController {

    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;

    public StatsApiController(DoctorRepository doctorRepo, PatientRepository patientRepo, AppointmentRepository appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @GetMapping("/api/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> byStatus = new LinkedHashMap<>();
        byStatus.put("BOOKED", appointmentRepo.countByStatus("BOOKED"));
        byStatus.put("COMPLETED", appointmentRepo.countByStatus("COMPLETED"));
        byStatus.put("CANCELLED", appointmentRepo.countByStatus("CANCELLED"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalDoctors", doctorRepo.count());
        result.put("totalPatients", patientRepo.count());
        result.put("totalAppointments", appointmentRepo.count());
        result.put("byStatus", byStatus);

        return ResponseEntity.ok(result);
    }
}
