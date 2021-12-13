package uni.project.mydocapp.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class AppointmentEntity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private UserEntity doctor;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private UserEntity patient;
	
	@Column(name="date", nullable = false)
	private String date;
	
	@Column(name = "hour")
	private int hour;
	
	@Column(name="reason")
	private String reason;
	
	@Column(name="status")
	private String status;

	public AppointmentEntity() {
	}
	
	public AppointmentEntity(UserEntity doctor, UserEntity patient, String date, int hour, String reason) {
		this.doctor = doctor;
		this.patient = patient;
		this.date = date;
		this.hour = hour;
		this.reason = reason;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(UserEntity doctor) {
		this.doctor = doctor;
	}

	public UserEntity getPatient() {
		return patient;
	}

	public void setPatient(UserEntity patient) {
		this.patient = patient;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
