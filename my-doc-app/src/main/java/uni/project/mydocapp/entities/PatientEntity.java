package uni.project.mydocapp.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("patient")
public class PatientEntity extends UserEntity{
	
	public PatientEntity() {
	}

	public PatientEntity(String name, String email, String password, int age) {
		super(name, email, password, age);
	}
	
}
