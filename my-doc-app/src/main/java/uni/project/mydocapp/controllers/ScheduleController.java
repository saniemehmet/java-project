package uni.project.mydocapp.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.entities.UserEntity;
import uni.project.mydocapp.entities.WorkingScheduleEntity;
import uni.project.mydocapp.repositories.ScheduleRepository;

@RestController
public class ScheduleController {
	private ScheduleRepository scheduleRepository;
	
	public ScheduleController(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}
	
	@GetMapping(path = "/schedule")
	public List<WorkingScheduleEntity> getAllWorkingSchedule(HttpSession session){
		DoctorEntity doctor = (DoctorEntity) session.getAttribute("user");
		if(doctor != null) {
			return scheduleRepository.findAllByDoctor(doctor);
		}
		return null;
	}
	
	@PostMapping(path = "/schedule/add")
	public WorkingScheduleEntity addSchedule(@RequestParam(value = "date") String dateString,
							  @RequestParam(value = "fromHour") int fromHour,
							  @RequestParam(value = "toHour") int toHour, HttpSession session) {
		DoctorEntity doctor = (DoctorEntity) session.getAttribute("user");
		if(doctor != null) {
			Date date;
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
			} catch (ParseException e) {
				return null;
			}  
			WorkingScheduleEntity schedule = new WorkingScheduleEntity(doctor, date, fromHour, toHour);
			return scheduleRepository.saveAndFlush(schedule);
		}
		return null;
	}
}
