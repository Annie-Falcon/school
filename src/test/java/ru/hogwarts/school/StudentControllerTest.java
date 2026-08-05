package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controllers.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private StudentController studentController;

    final String name1 = "СтудентТест1";
    final int age1 = 18;
    final long id1 = 1;
    final Long idNotExists = -1L;
    Student student1;
    List<Student> students;

    @BeforeEach
    public void setUp() {
        student1 = new Student();
        student1.setId(id1);
        student1.setAge(age1);
        student1.setName(name1);

        students = new ArrayList<>();
        students.add(student1);
    }

    @Test
    public void saveStudentTest() throws Exception {
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name1);
        studentObject.put("age", age1);

        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.age").value(age1));
    }

    @Test
    public void editStudentTest() throws Exception {
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name1);
        studentObject.put("age", age1);

        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.age").value(age1));
    }

    @Test
    public void getStudentByIdTest() throws Exception {

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.age").value(age1));
    }

    @Test
    public void getStudentsAllTest() throws Exception {

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[0].age").value(age1));
    }

    @Test
    public void getStudensByAgetTest() throws Exception {

        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?min=17&max=20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[0].age").value(age1));
    }

    @Test
    public void getFacultyErrWayTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getNoFacultyByIdTest() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + idNotExists)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
