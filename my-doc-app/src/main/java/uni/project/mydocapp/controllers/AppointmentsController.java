package uni.project.mydocapp.controllers;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.project.mydocapp.entities.AppointmentEntity;
import uni.project.mydocapp.entities.UserEntity;
import uni.project.mydocapp.repositories.AppointmentRepository;
import uni.project.mydocapp.repositories.UserRepository;

@RestController
public class AppointmentsController {
	private AppointmentRepository appointmentRepository;
	private UserRepository userRepository;
	
	public AppointmentsController(AppointmentRepository appointmentRepository, UserRepository userRepository) {
		this.appointmentRepository = appointmentRepository;
		this.userRepository = userRepository;
	}
	
	@PostMapping(path = "appointment/add")
	public AppointmentEntity addAppointment(@RequestParam(value = "doctorId") int id,
								 @RequestParam(value = "date") String date,
								 @RequestParam(value ="hour") int hour,
								 @RequestParam(value = "condition") String condition, 
								 HttpSession session){
		UserEntity patient = (UserEntity) session.getAttribute("user");
		Optional<UserEntity> optionalDoctor = userRepository.findById(id);
		if(optionalDoctor.isPresent()) {
			UserEntity doctor = (UserEntity) optionalDoctor.get();
			if(patient!=null) {
				AppointmentEntity appointment = new AppointmentEntity(doctor, patient, date, hour, condition);
				return appointmentRepository.saveAndFlush(appointment);
			}
		}
		return null;
	}
}
