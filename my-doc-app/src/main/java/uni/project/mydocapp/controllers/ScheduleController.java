package uni.project.mydocapp.controllers;

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

import uni.project.mydocapp.comparators.ScheduleDateComparator;
import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.entities.ScheduleEntity;
import uni.project.mydocapp.repositories.ScheduleRepository;

@RestController
public class ScheduleController {
	private ScheduleRepository scheduleRepository;
	
	public ScheduleController(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}
	
	@GetMapping(path = "/schedule")
	public List<ScheduleEntity> getAllWorkingSchedule(HttpSession session){
		DoctorEntity doctor = (DoctorEntity) session.getAttribute("user");
		if(doctor != null) {
			List<ScheduleEntity> allSchedules = scheduleRepository.findAllByDoctor(doctor);
			Collections.sort(allSchedules, new ScheduleDateComparator());
			return allSchedules;
		}
		return null;
	}
	
	@PostMapping(path = "/schedule/add")
	public ScheduleEntity addSchedule(@RequestParam(value = "date") String date,
							  @RequestParam(value = "fromHour") int fromHour,
							  @RequestParam(value = "toHour") int toHour, HttpSession session) {
		DoctorEntity doctor = (DoctorEntity) session.getAttribute("user");
		List<ScheduleEntity> existing = getAllWorkingSchedule(date, session);
		if(doctor != null && (existing.size() == 0 || existing == null)) {
			ScheduleEntity schedule = new ScheduleEntity(doctor, date, fromHour, toHour);
			return scheduleRepository.saveAndFlush(schedule);
		}
		return null;
	}
	
	@PostMapping(path = "/schedule/edit")
	public ScheduleEntity editSchedule(@RequestParam(value = "date") String date,
							  				  @RequestParam(value = "fromHour") int fromHour,
							  				  @RequestParam(value = "toHour") int toHour, HttpSession session) {
		DoctorEntity doctor = (DoctorEntity) session.getAttribute("user");
		Optional<ScheduleEntity> optionalSchedule = scheduleRepository.findByDoctorAndDate(doctor, date);
		if(optionalSchedule.isPresent()) {
			ScheduleEntity schedule = optionalSchedule.get();
			if(schedule.getDoctor().getId() == doctor.getId()) {
				schedule.setDate(date);
				schedule.setFromHour(fromHour);
				schedule.setToHour(toHour);
				return scheduleRepository.save(schedule);
			}
		}
		return null;
	}
	
	@DeleteMapping(path = "/schedule/delete")
	public ResponseEntity<Boolean> deleteSchedule(@RequestParam(value = "id") int id, HttpSession session){
		DoctorEntity doctor = (DoctorEntity) session.getAttribute("user");
		if(doctor == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
		Optional<ScheduleEntity> optionalSchedule = scheduleRepository.findById(id);
		if(optionalSchedule.isPresent()) {
			ScheduleEntity schedule = optionalSchedule.get();
			if(schedule.getDoctor().getId() == doctor.getId()) {
				scheduleRepository.delete(schedule);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}else {
				return new ResponseEntity<Boolean>(false, HttpStatus.FORBIDDEN);
			}
		}else {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(path = "schedule/search")
	public List<ScheduleEntity> getAllWorkingSchedule(@RequestParam(value = "date") String date, HttpSession session){
		DoctorEntity doctor = (DoctorEntity) session.getAttribute("user");
		if(doctor != null) {
			List<ScheduleEntity> allSchedules = scheduleRepository.findByDateAndDoctor(date, doctor);
			Collections.sort(allSchedules, new ScheduleDateComparator());
			return allSchedules;
		}
		return null;
	}
	
}
