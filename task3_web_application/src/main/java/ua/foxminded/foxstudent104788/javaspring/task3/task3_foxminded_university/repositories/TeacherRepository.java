package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;

/**
 * This interface defines methods for interacting with teachers in the database.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

}
