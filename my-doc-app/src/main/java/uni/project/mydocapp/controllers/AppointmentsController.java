package uni.project.mydocapp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
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
				AppointmentEntity appointment = new AppointmentEntity(doctor, patient, date, hour, condition, "Pending");
				return appointmentRepository.saveAndFlush(appointment);
			}
		}
		return null;
	}
	
	@GetMapping(path = "appointment/find/reserved")
	public List<Integer> findReservedAppointmentsByDoctorAndDate(@RequestParam(value = "doctorId") int id, 
																 @RequestParam(value = "date") String date){
		List<Integer> reservedHours = new ArrayList<Integer>();
		Optional<UserEntity> optionalDoctor = userRepository.findById(id);
		if(optionalDoctor.isPresent()) {
			UserEntity doctor = (UserEntity) optionalDoctor.get();
			List<AppointmentEntity> reservedAppointments = appointmentRepository.findByDoctorAndDate(doctor, date);
			if(reservedAppointments!=null) {
				for(int i=0;i<reservedAppointments.size();i++) {
					if(!reservedAppointments.get(i).getStatus().toLowerCase().equals("rejected")) {
						reservedHours.add(reservedAppointments.get(i).getHour());
					}
				}	
				return reservedHours;
			}
		}
		return null;
	}
	
	@GetMapping(path = "appointment/find")
	public AppointmentEntity checkIfAppointmentExistsForUser(@RequestParam(value = "date") String date, HttpSession session) {
		UserEntity patient = (UserEntity) session.getAttribute("user");
		Optional<AppointmentEntity> appointmentOptional = appointmentRepository.findByPatientAndDate(patient, date);
		if(appointmentOptional.isPresent()) {
			return appointmentOptional.get();
		}
		return null;
	}
}
