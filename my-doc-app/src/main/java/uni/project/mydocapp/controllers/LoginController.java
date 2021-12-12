package uni.project.mydocapp.controllers;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.entities.PatientEntity;
import uni.project.mydocapp.entities.UserEntity;
import uni.project.mydocapp.repositories.UserRepository;

@RestController
public class LoginController {
	private UserRepository userRepository;
	
	public LoginController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@PostMapping(path = "/register")
	public UserEntity register( @RequestParam(value = "data") String dataString) {
		JSONObject data = new JSONObject(dataString);
		if(data.getString("password").equals(data.getString("repeatPassword"))) {
			if(data.getString("userType").equalsIgnoreCase("doctor")) {
				DoctorEntity doctor = new DoctorEntity(	data.getString("name"), data.getString("email"), 
														data.getString("password"), data.getInt("age"), 
														data.getString("specialty"), data.getInt("experience"),
														data.getString("location"), data.getString("overview"), 
														data.getString("contactDetails"));
				return userRepository.saveAndFlush(doctor);
			}else {
				PatientEntity patient = new PatientEntity(data.getString("name"), data.getString("email"), data.getString("password"), data.getInt("age"));
				return userRepository.saveAndFlush(patient);
			}
		}
		return null;
	}
	
	@PostMapping(path = "/login")
	public String login(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password, HttpSession session) {
		UserEntity user = userRepository.findUserByEmailAndPassword(email, password);
		if(user != null) {
			session.setAttribute("user", user);
			return user.getUserType();
		}
		return null;
	}
	
//	@GetMapping(path = "/loggedUserId")
//	public ResponseEntity<Integer> loggedUserOd(HttpSession session){
//		UserEntity user = (UserEntity) session.getAttribute("user");
//		if(user != null) {
//			return new ResponseEntity<Integer>(user.getId(), HttpStatus.OK);
//		}else {
//			return new ResponseEntity<Integer>(-1, HttpStatus.UNAUTHORIZED);
//		}
//	}
	
	@PostMapping(path = "/logout")
	public ResponseEntity<Boolean> logout(HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		if(user != null) {
			session.invalidate();
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
	}
}