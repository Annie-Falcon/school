package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty add(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void remove(long id) {
        facultyRepository.deleteById(id);
    }

    public Faculty update(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty get(long id) {
        return facultyRepository.findById(id).get();
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getByColor(String color) {
        if (!StringUtils.hasText(color)) {
            throw new IllegalArgumentException("Укажите корректный цвет факультета!");
        }
        return this.facultyRepository.findAll().stream().filter(e -> e.getColor().equals(color)).collect(Collectors.toList());
    }

    public Collection<Faculty> findBooksByNameOrColor(String name, String color) {
        return facultyRepository.findBooksByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }
}
