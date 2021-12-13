package uni.project.mydocapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.entities.WorkingScheduleEntity;

@Repository
public interface ScheduleRepository extends JpaRepository<WorkingScheduleEntity, Integer> {

	List<WorkingScheduleEntity> findAllByDoctor(DoctorEntity doctor);

	Optional<WorkingScheduleEntity> findByDate(String date);

	List<WorkingScheduleEntity> findByDateAndDoctor(String date, DoctorEntity doctor);

	List<WorkingScheduleEntity> findByIdAndDate(int id, String date);

	Optional<WorkingScheduleEntity> findByDoctorAndDate(DoctorEntity doctor, String date);

}
