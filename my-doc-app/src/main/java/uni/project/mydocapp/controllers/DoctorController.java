package uni.project.mydocapp.controllers;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.repositories.UserRepository;

@RestController
public class DoctorController {
	
	private UserRepository userRepository;
	public DoctorController(UserRepository userRepository) {
		this.userRepository = userRepository;
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
}
