package uni.project.mydocapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.project.mydocapp.entities.AppointmentEntity;
import uni.project.mydocapp.entities.UserEntity;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {

	List<AppointmentEntity> findByDoctorAndDate(UserEntity doctor, String date);

	Optional<AppointmentEntity> findByPatientAndDate(UserEntity patient, String date);
	
}
