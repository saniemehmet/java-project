package uni.project.mydocapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.entities.ScheduleEntity;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {

	List<ScheduleEntity> findAllByDoctor(DoctorEntity doctor);

	Optional<ScheduleEntity> findByDate(String date);

	List<ScheduleEntity> findByDateAndDoctor(String date, DoctorEntity doctor);

	List<ScheduleEntity> findByIdAndDate(int id, String date);

	Optional<ScheduleEntity> findByDoctorAndDate(DoctorEntity doctor, String date);

}
