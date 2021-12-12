package uni.project.mydocapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.project.mydocapp.entities.DoctorEntity;
import uni.project.mydocapp.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	UserEntity findUserByEmailAndPassword(String email, String password);

	List<DoctorEntity> findAllByUserType(String string);

	List<DoctorEntity> findAllByUserTypeAndLocationIgnoreCaseContainingAndSpecialtiesIgnoreCaseContaining(String userType,
			String location, String specialty);

	List<DoctorEntity> findAllByUserTypeAndLocationIgnoreCaseContaining(String userType, String location);

	List<DoctorEntity> findAllByUserTypeAndSpecialtiesIgnoreCaseContaining(String userType, String specialty);
}
