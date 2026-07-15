package ru.hogwarts.school.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping // POST https://localhost:8080/student
    @ManagedOperation(description = "добавление студентов")
    public Student addStudent(@RequestBody Student student) {
        return studentService.add(student);
    }

    @GetMapping("{id}") // GET https://localhost:8080/student/23
    @ManagedOperation(description = "получение студента по id")
    public ResponseEntity getStudent(@PathVariable Long id) {
        Student student = studentService.get(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping // GET https://localhost:8080/student/
    @ManagedOperation(description = "получение списка студентов")
    public ResponseEntity<Collection<Student>> getStudents(@RequestParam(required = false) Integer min,
                                                           @RequestParam(required = false) Integer max) {
        if (min != null && max != null) {
            return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
        }
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("faculty/{studentId}")
    @ManagedOperation(description = "получение факультета студента")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long studentId) {
        Faculty faculty = studentService.get(studentId).getFaculty();
        return ResponseEntity.ok(faculty);
    }

    @PutMapping // PUT https://localhost:8080/student
    @ManagedOperation(description = "редактирование студента")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.update(student);
        if (foundStudent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}") // DELETE https://localhost:8080/student/23
    @ManagedOperation(description = "удаление студента")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("age") // GET https://localhost:8080/student/age/19
    @ManagedOperation(description = "получение студентов по возрасту")
    public ResponseEntity<Collection<Student>> getStudentsByColor(@RequestParam(required = false) int age) {
        return ResponseEntity.ok(studentService.getByAge(age));
    }
}
