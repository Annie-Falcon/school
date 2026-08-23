package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty add(Faculty faculty) {
        logger.info("method add called");
        return facultyRepository.save(faculty);
    }

    public void remove(long id) {
        logger.info("method remove called");
        facultyRepository.deleteById(id);
    }

    public Faculty update(Faculty faculty) {
        logger.info("method update called");
        return facultyRepository.save(faculty);
    }

    public Faculty get(long id) {
        logger.info("method get called");
        if (facultyRepository.findById(id).isEmpty()) {
            logger.error("There is not faculty with id = {}", id);
            return new Faculty();
        } else {
            return facultyRepository.findById(id).get();
        }
    }

    public Collection<Faculty> getAll() {
        logger.info("method getAll called");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getByColor(String color) {
        logger.info("method getByColor called");
        if (!StringUtils.hasText(color)) {
            logger.error("Incorrect faculty color is specified: {}", color);
            throw new IllegalArgumentException("Укажите корректный цвет факультета!");
        }
        return this.facultyRepository.findAll().stream().filter(e -> e.getColor().equals(color)).collect(Collectors.toList());
    }

    public Collection<Faculty> findBooksByNameOrColor(String name, String color) {
        logger.info("method findBooksByNameOrColor called");
        return facultyRepository.findBooksByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public long findIdByNameAndColor(String name, String color) {
        logger.info("method findIdByNameAndColor called");
        Collection<Faculty> tempFaculties = this.facultyRepository.findAll().stream()
                .filter(e -> e.getColor().equals(color) && e.getName().equals(name))
                .collect(Collectors.toList());

        if (tempFaculties.isEmpty()) {
            logger.error("An empty list is displayed for the specified parameters: name={}; color={}", name, color);
            return -1L;
        } else {
            return tempFaculties.stream().findFirst().get().getId();
        }
    }
}
