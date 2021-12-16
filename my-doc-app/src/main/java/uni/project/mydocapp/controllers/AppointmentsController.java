package uni.project.mydocapp.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.project.mydocapp.comparators.AppointmentDateComparator;
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
	
	@PostMapping(path = "/appointment/add")
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
	
	@GetMapping(path = "/appointment/find/reserved")
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
	
	@GetMapping(path = "/appointment/find")
	public AppointmentEntity checkIfAppointmentExistsForUser(@RequestParam(value = "date") String date,
															 @RequestParam(value = "doctorId") int doctorId, 
															 HttpSession session) {
		UserEntity patient = (UserEntity) session.getAttribute("user");
		Optional<UserEntity> doctor = userRepository.findById(doctorId);
		if(doctor.isPresent()) {
			Optional<AppointmentEntity> appointmentOptional = appointmentRepository.findByDoctorAndPatientAndDate(doctor.get(), patient, date);
			if(appointmentOptional.isPresent()) {
				return appointmentOptional.get();
			}
		}
		return null;
	}
	
	@GetMapping(path = "/appointments/all")
	public List<AppointmentEntity> getAllAppointmentsByUser(HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		if(user!=null) {
			if(user.getUserType().toLowerCase().equals("patient")) {
				List<AppointmentEntity> allPatientAppointments = appointmentRepository.findAllByPatient(user);	
				Collections.sort(allPatientAppointments, new AppointmentDateComparator());
				return allPatientAppointments;
			}else {
				List<AppointmentEntity> allDoctorAppointments = appointmentRepository.findAllByDoctor(user);
				Collections.sort(allDoctorAppointments, new AppointmentDateComparator());
				return allDoctorAppointments;
			}
		}
		return null;
	}
	
	@PostMapping(path = "appointment/edit")
	public AppointmentEntity editAppointment(@RequestParam(value = "doctorId") int doctorId,
											 @RequestParam(value = "date") String date,
											 @RequestParam(value = "hour") int hour, HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Optional<UserEntity> doctor = userRepository.findById(doctorId);
		if(user != null && doctor.isPresent()) {
			Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findByDoctorAndPatientAndDate(doctor.get(), user, date);
			if(optionalAppointment.isPresent()) {
				AppointmentEntity appointment = optionalAppointment.get();
				if(appointment.getPatient().getId() == user.getId()) {
					appointment.setHour(hour);
					return appointmentRepository.save(appointment);
				}
			}
		}
		return null;
	}
	
	@DeleteMapping(path = "appointment/delete")
	public ResponseEntity<Boolean> deleteAppointment(@RequestParam(value = "doctorId") int doctorId,
											 @RequestParam(value = "date") String date, HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Optional<UserEntity> doctor = userRepository.findById(doctorId);
		if(user != null && doctor.isPresent()) {
			Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findByDoctorAndPatientAndDate(doctor.get(), user, date);
			if(optionalAppointment.isPresent()) {
				AppointmentEntity appointment = optionalAppointment.get();
				if(appointment.getPatient().getId() == user.getId()) {
					appointmentRepository.delete(appointment);
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@PostMapping(path = "appointment/approve")
	public AppointmentEntity approveAppointment(@RequestParam(value = "patientId") int patientId,
												@RequestParam(value = "date") String date,
												HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Optional<UserEntity> patient = userRepository.findById(patientId);
		if(user != null && patient.isPresent()) {
			Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findByDoctorAndPatientAndDate(user, patient.get(), date);
			if(optionalAppointment.isPresent()) {
				AppointmentEntity appointment = optionalAppointment.get();
				if(appointment.getDoctor().getId() == user.getId()) {
					appointment.setStatus("Approved");
					return appointmentRepository.save(appointment);
				}
			}
		}
		return null;
	}
	
	@PostMapping(path = "appointment/reject")
	public AppointmentEntity rejectAppointment( @RequestParam(value = "patientId") int patientId,
												@RequestParam(value = "date") String date,
												HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Optional<UserEntity> patient = userRepository.findById(patientId);
		if(user != null && patient.isPresent()) {
			Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findByDoctorAndPatientAndDate(user, patient.get(), date);
			if(optionalAppointment.isPresent()) {
				AppointmentEntity appointment = optionalAppointment.get();
				if(appointment.getDoctor().getId() == user.getId()) {
					appointment.setStatus("Rejected");
					return appointmentRepository.save(appointment);
				}
			}
		}
		return null;
	}
}
