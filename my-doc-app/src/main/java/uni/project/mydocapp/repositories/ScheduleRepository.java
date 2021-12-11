package uni.project.mydocapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.entities.WorkingScheduleEntity;

public interface ScheduleRepository extends JpaRepository<WorkingScheduleEntity, Integer> {

	List<WorkingScheduleEntity> findAllByDoctor(DoctorEntity doctor);
}
