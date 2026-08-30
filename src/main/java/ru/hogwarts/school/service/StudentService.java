package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student add(Student student) {
        logger.info("method add called");
        return studentRepository.save(student);
    }

    public void remove(long id) {
        logger.info("method remove called");
        studentRepository.deleteById(id);
    }

    public Student update(Student student) {
        logger.info("method update called");
        return studentRepository.save(student);
    }

    public Student get(long id) {
        logger.info("method get called");
        if (studentRepository.findById(id).isEmpty()) {
            logger.error("There is not student with id = {}", id);
            return new Student();
        } else {
            return studentRepository.findById(id).get();
        }
    }

    public Collection<Student> getAll() {
        logger.info("method getAll called");
        return studentRepository.findAll();
    }

    public Collection<Student> getByAge(int age) {
        logger.info("method getByAge called");
        if (age <= 10 || age >= 100) {
            logger.error("The specified age {} does not fall within the range 10:100", age);
            throw new IllegalArgumentException("Укажите корректный возраст студента!");
        }
        return this.studentRepository.findAll().stream().filter(e -> e.getAge().equals(age)).collect(Collectors.toList());
    }

    public Collection<Student> findByAgeBetween(Integer min, Integer max) {
        logger.info("method findByAgeBetween called");
        return studentRepository.findByAgeBetween(min, max);
    }

    public long findIdByNameAndAge(String name, Integer age) {
        logger.info("method findIdByNameAndAge called");
        Collection<Student> tempStudents = this.studentRepository.findAll().stream()
                .filter(e -> e.getAge().equals(age) && e.getName().equals(name))
                .toList();

        if (tempStudents.isEmpty()) {
            logger.error("An empty list is displayed for the specified parameters: name={}; age={}", name, age);
            return -1L;
        } else {
            return tempStudents.stream().findFirst().get().getId();
        }
    }

    public Integer getCountAllStudent() {
        logger.info("method getCountAllStudent called");
        return studentRepository.getCountAllStudent();
    }

    public Integer getAvgAgeStudent() {
        logger.info("method getAvgAgeStudent called");
        return studentRepository.getAvgAgeStudent();
    }

    public List<Student> getFiveLastStudents() {
        logger.info("method getFiveLastStudents called");
        return studentRepository.getFiveLastStudents();
    }

    public List<Student> getStudentsNameStartA() {
        logger.info("method getStudentsNameStartA called");
        return studentRepository.findAll()
                .stream()
                .filter(i -> i.getName().toUpperCase().startsWith("М"))
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }

    public int getAvgAgeStudentStream() {
        logger.info("method getAvgAgeStudentStream called");
        return (int) Math.round(studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0));
    }
}
