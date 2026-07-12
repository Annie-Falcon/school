package ru.hogwarts.school.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping // POST https://localhost:8080/faculty
    @ManagedOperation(description = "добавление факультета")
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.add(faculty);
    }

    @GetMapping("{id}") // GET https://localhost:8080/faculty/23
    @ManagedOperation(description = "получение факультета по id")
    public ResponseEntity getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.get(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping// GET https://localhost:8080/faculty/all
    @ManagedOperation(description = "получение всех факультетов")
    public ResponseEntity<Collection<Faculty>> getFaculties(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String color) {
        if ((name != null && !name.isBlank()) || (color != null && !color.isBlank())) {
            return ResponseEntity.ok(facultyService.findBooksByNameOrColor(name, color));
        }
        return ResponseEntity.ok(facultyService.getAll());
    }

    @GetMapping("sudents/{facultyId}")
    @ManagedOperation(description = "получение студентов факультета")
    public ResponseEntity<Collection<Student>> getStudents(@PathVariable Long facultyId) {
        Collection<Student> students = facultyService.get(facultyId).getStudents();
        return ResponseEntity.ok(students);
    }

    @PutMapping  // PUT https://localhost:8080/faculty
    @ManagedOperation(description = "редактирование факультета")
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.update(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}") // DELETE https://localhost:8080/faculty/23
    @ManagedOperation(description = "удаление факультета")
    public ResponseEntity deleteFaculty(@PathVariable Long id) {
        facultyService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("color") // GET https://localhost:8080/faculty/color/green
    @ManagedOperation(description = "получение факультетов по цвету")
    public ResponseEntity<Collection<Faculty>> getFacultiesByColor(@RequestParam String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.getByColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}
