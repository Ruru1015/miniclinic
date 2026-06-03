package tw.edu.fju.miniclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;
import java.util.List;

@Controller
public class PatientController {

    private final PatientRepository patientRepo;

    public PatientController(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    // 網頁頁面：GET /patients
    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", patientRepo.findAll());
        return "patients"; // 對應 templates/patients.html
    }

    // REST API：GET /api/patients
    @GetMapping("/api/patients")
    @ResponseBody // 讓 @Controller 也能回傳 JSON 資料
    public List<Patient> getPatientsApi() {
        return patientRepo.findAll();
    }
}