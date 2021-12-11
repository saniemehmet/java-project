package uni.project.mydocapp.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "working_schedule")
public class WorkingScheduleEntity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "doctor_id")
	private DoctorEntity doctor;
	
	@Column(name="date", nullable = false)
	private Date date;
	
	@Column(name = "from_hour", nullable = false)
	private int fromHour;

	@Column(name = "to_hour", nullable = false)
	private int toHour;
	
	public WorkingScheduleEntity() {
	}
	
	public WorkingScheduleEntity(DoctorEntity doctor, Date date, int fromHour, int toHour) {
		this.doctor = doctor;
		this.date = date;
		this.fromHour = fromHour;
		this.toHour = toHour;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getFromHour() {
		return fromHour;
	}

	public void setFromHour(int fromHour) {
		this.fromHour = fromHour;
	}

	public int getToHour() {
		return toHour;
	}

	public void setToHour(int toHour) {
		this.toHour = toHour;
	}
	
	
}
