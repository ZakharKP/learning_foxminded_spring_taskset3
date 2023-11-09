package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;

@Repository
public interface LoginDataRepository extends JpaRepository<LoginData, String> {

}
