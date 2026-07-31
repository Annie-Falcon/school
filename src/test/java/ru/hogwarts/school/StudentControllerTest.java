package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controllers.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    Student studentTest;
    long idStudentTest;

    Student studentDel;
    long idStudentDel;

    @BeforeEach
    void SetUp() {
        idStudentTest = studentService.findIdByNameAndAge("МашаTest", 20);
        if (idStudentTest == -1) {
            studentTest = new Student();
            studentTest.setName("МашаTest");
            studentTest.setAge(20);
            studentTest = studentService.add(studentTest);
            idStudentTest = studentTest.getId();
        } else {
            studentTest = studentService.get(idStudentTest);
        }

        idStudentDel = studentService.findIdByNameAndAge("МашаDel", 20);
        if (idStudentDel == -1) {
            studentDel = new Student();
            studentDel.setName("МашаDel");
            studentDel.setAge(20);
            studentDel = studentService.add(studentDel);
            idStudentDel = studentDel.getId();
        } else {
            studentDel = studentService.get(idStudentDel);
        }
    }

    @Test
    public void testGetStudentById() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + idStudentTest, String.class))
                .contains("МашаTest");
    }

    @Test
    public void testGetAllStudent() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isNotEmpty();
    }

    @Test
    public void testGetAgeStudent() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student?min=18&max=21", String.class))
                .contains("МашаTest");
    }

    @Test
    public void testGetStudentByAge() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student?age=20", String.class))
                .contains("МашаTest");
    }

    @Test
    public void testPostStudent() throws Exception {
        Student studentPost = new Student();
        studentPost.setName("ФедорPost");
        studentPost.setAge(14);

        Assertions.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", studentPost, String.class))
                .isNotNull();

        long idStudentPost = studentService.findIdByNameAndAge("ФедорPost", 14);
        studentService.remove(idStudentPost);
    }

    @Test
    public void testPutStudent() throws Exception {
        studentTest.setName("МашаTestМ");
        restTemplate.put("http://localhost:" + port + "/student", studentTest);
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + idStudentTest, String.class))
                .contains("МашаTestМ");

        long idStudentPut = studentService.findIdByNameAndAge("МашаTestМ", 20);
        studentService.remove(idStudentPut);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + idStudentDel, String.class))
                .isNotNull();

        restTemplate.delete("http://localhost:" + port + "/student/" + idStudentDel);

        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + idStudentDel, String.class))
                .contains(":500");
    }
}
