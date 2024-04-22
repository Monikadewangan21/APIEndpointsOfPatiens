package cm.assinment.apiendpoints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class OutpatientAppointmentSystemApplication {

    private List<Doctor> doctors = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(OutpatientAppointmentSystemApplication.class, args);
    }

    @GetMapping("/doctors")
    public List<Doctor> getDoctors() {
        return doctors;
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable int id) {
        Doctor doctor = doctors.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/appointments")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentRequest request) {
        Doctor doctor = doctors.stream().filter(d -> d.getId() == request.getDoctorId()).findFirst().orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        if (!doctor.getPracticeDays().contains(request.getAppointmentDate())) {
            return ResponseEntity.badRequest().body("Doctor doesn't practice on this day");
        }

        long bookedAppointmentsCount = appointments.stream()
                .filter(appt -> appt.getDoctorId() == request.getDoctorId() && appt.getAppointmentDate().equals(request.getAppointmentDate()))
                .count();

        if (bookedAppointmentsCount >= doctor.getConsultationLimit()) {
            return ResponseEntity.badRequest().body("Doctor has no more available appointments for this day");
        }

        appointments.add(new Appointment(request.getDoctorId(), request.getAppointmentDate()));
        return ResponseEntity.ok("Appointment booked successfully");
    }
}

class Doctor {
    private int id;
    private String name;
    private String location;
    private List<String> practiceDays;
    private int consultationLimit;
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	public long getConsultationLimit() {
		// TODO Auto-generated method stub
		return 0;
	}
	public List<Doctor> getPracticeDays() {
		// TODO Auto-generated method stub
		return null;
	}

    // Constructor, getters, and setters
}

class Appointment {
    private int doctorId;
    private String appointmentDate;
	public Appointment(int doctorId2, Object appointmentDate2) {
		// TODO Auto-generated constructor stub
	}
	public int getDoctorId() {
		// TODO Auto-generated method stub
		return 0;
	}
	public Object getAppointmentDate() {
		// TODO Auto-generated method stub
		return null;
	}

    // Constructor, getters, and setters
}

class AppointmentRequest {
    private int doctorId;
    private String appointmentDate;
	public int getDoctorId() {
		// TODO Auto-generated method stub
		return 0;
	}
	public Object getAppointmentDate() {
		// TODO Auto-generated method stub
		return null;
	}

    // Constructor, getters, and setters
}
