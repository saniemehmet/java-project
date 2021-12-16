package uni.project.mydocapp.controllers;

import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.entities.UserEntity;
import uni.project.mydocapp.entities.ScheduleEntity;
import uni.project.mydocapp.repositories.ScheduleRepository;
import uni.project.mydocapp.repositories.UserRepository;

@RestController
public class DoctorsController {
	
	private UserRepository userRepository;
	private ScheduleRepository scheduleRepository;
	
	public DoctorsController(UserRepository userRepository, ScheduleRepository scheduleRepository) {
		this.userRepository = userRepository;
		this.scheduleRepository = scheduleRepository;
	}
	
	@GetMapping(path = "/doctors/all")
	public List<DoctorEntity> getAllDoctors(){
		return userRepository.findAllByUserType("doctor");
	}
	
	@GetMapping(path = "/doctors/search")
	public List<DoctorEntity> findDoctorsByLocationAndSpecialty(@RequestParam(value = "data") String data){
		JSONObject searchData = new JSONObject(data);
		String location = null, specialty = null;
		try{
			location = searchData.getString("location");
		}catch (JSONException e) {
			location = "";
		}
		try{
			specialty = searchData.getString("specialty");
		}catch (JSONException e) {
			specialty = "";
		}
		if(location != "" && specialty != "") {
			return userRepository.findAllByUserTypeAndLocationIgnoreCaseContainingAndSpecialtiesIgnoreCaseContaining("doctor", location, specialty);
		}else if(location!="") {
			return userRepository.findAllByUserTypeAndLocationIgnoreCaseContaining("doctor", location);
		}else if(specialty!="") {
			return userRepository.findAllByUserTypeAndSpecialtiesIgnoreCaseContaining("doctor", specialty);
		}
		return null;
	}
	
	@GetMapping(path = "/schedule/doctor/search")
	public ScheduleEntity getDoctorSchedule(@RequestParam(value = "doctorId") int doctorId, @RequestParam(value = "date") String date){
		Optional<UserEntity> optionalUser = userRepository.findById(doctorId);
		if(optionalUser.isPresent()) {
			DoctorEntity doctor = (DoctorEntity) optionalUser.get();
			if(doctor != null) {
				Optional<ScheduleEntity> optionalSchedule = scheduleRepository.findByDoctorAndDate(doctor, date);
				if(optionalSchedule.isPresent()) {
					return optionalSchedule.get();
				}else {
					return null;
				}
			}
		}
		return null;
	}
}
