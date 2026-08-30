package ru.hogwarts.school.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping("{id}") // GET https://localhost:8080/student/23
    @ManagedOperation(description = "получение студента по id")
    public ResponseEntity getStudent(@PathVariable Long id) {
        Student student = studentService.get(id);
        if (student == null || student.equals(new Student())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

    @GetMapping("age") // GET https://localhost:8080/student/age/19
    @ManagedOperation(description = "получение студентов по возрасту")
    public ResponseEntity<Collection<Student>> getStudentsByColor(@RequestParam(required = false) int age) {
        return ResponseEntity.ok(studentService.getByAge(age));
    }

    @PostMapping // POST https://localhost:8080/student
    @ManagedOperation(description = "добавление студентов")
    public Student addStudent(@RequestBody Student student) {
        return studentService.add(student);
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

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaTYpe()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaTYpe());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("/get-count-all-student")
    public Integer getCountAllStudent() {
        return studentService.getCountAllStudent();
    }

    @GetMapping("/get-avg-age-student")
    public Integer getAvgAgeStudent() {
        return studentService.getAvgAgeStudent();
    }

    @GetMapping("/get-five-last-students")
    public List<Student> getFiveLastStudents() {
        return studentService.getFiveLastStudents();
    }

    @GetMapping("/get-students-name-start-A")
    public List<Student> getStudentsNameStartA() {
        return studentService.getStudentsNameStartA();
    }

    @GetMapping("/get-avg-age-student-stream")
    public int getAvgAgeStudentStream() {
        return studentService.getAvgAgeStudentStream();
    }
}
