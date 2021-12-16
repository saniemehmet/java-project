package uni.project.mydocapp.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("doctor")
@JsonIgnoreProperties({"schedule"})
public class DoctorEntity extends UserEntity{
	
	@Column(name = "specialties", length = 500)
	private String specialties;
	
	@Column(name = "experience")
	private int experience;
	
	@Column(name = "location", length = 255)
	private String location;
	
	@Column(name = "overview", length = 2000)
	private String overview;
	
	@Column(name = "contact_details", length = 255)
	private String contactDetails;
	
	@OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
	private List<ScheduleEntity> schedule;
	
	public DoctorEntity() {
		
	}
	
	public DoctorEntity(String name, String email, String password, int age, String specialties, int experience, String location, String overview, String contactDetails) {
		super(name, email, password, age);
		this.specialties = specialties;
		this.experience = experience;
		this.location = location;
		this.overview = overview;
		this.contactDetails = contactDetails;
	}

	public String getSpecialties() {
		return specialties;
	}

	public void setSpecialties(String specialties) {
		this.specialties = specialties;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public List<ScheduleEntity> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<ScheduleEntity> schedule) {
		this.schedule = schedule;
	}
}
