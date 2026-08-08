package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Collection<Student>  findByAgeBetween(Integer min, Integer max);

    @Query(value = "SELECT COUNT(*) as student_number FROM student", nativeQuery = true)
    Integer getCountAllStudent();

    @Query(value = "SELECT COALESCE(AVG(age),0) as average_age FROM student where age > 25", nativeQuery = true)
    Integer getAvgAgeStudent();

    @Query(value = "SELECT id, name, age, faculty_id FROM student ORDER BY ID DESC LIMIT 5", nativeQuery = true)
    List<Student> getFiveLastStudents();
}
