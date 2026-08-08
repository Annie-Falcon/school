package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import ru.hogwarts.school.controllers.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    FacultyService facultyService;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    Faculty facultyTest;
    long idFacultyTest;

    Faculty facultyDel;
    long idFacultyDel;
    long idNotExists = -1L;

    @BeforeEach
    void SetUp() {
        idFacultyTest = facultyService.findIdByNameAndColor("FTest", "ColorTest");
        if (idFacultyTest == -1) {
            facultyTest = new Faculty();
            facultyTest.setName("FTest");
            facultyTest.setColor("ColorTest");
            facultyTest = facultyService.add(facultyTest);
            idFacultyTest = facultyTest.getId();
        } else {
            facultyTest = facultyService.get(idFacultyTest);
        }

        idFacultyDel = facultyService.findIdByNameAndColor("FTestDel", "ColorTest");
        if (idFacultyDel == -1) {
            facultyDel = new Faculty();
            facultyDel.setName("FTestDel");
            facultyDel.setColor("ColorTest");
            facultyDel = facultyService.add(facultyDel);
            idFacultyDel = facultyDel.getId();
        } else {
            facultyDel = facultyService.get(idFacultyDel);
        }
    }

    @Test
    public void testGetFacultyById() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + idFacultyTest, String.class))
                .contains("FTest");
    }

    @Test
    public void testGetFacultyByIdNotFound() throws Exception {
        try {
            restTemplate.getForObject("http://localhost:" + port + "/faculty/" + idNotExists, String.class);
        } catch (HttpClientErrorException ex) {
            Assertions.assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testGetAllFaculty() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class))
                .isNotEmpty();
    }

    @Test
    public void testPostFaculty() throws Exception {
        Faculty facultyPost = new Faculty();
        facultyPost.setName("FTestPost");
        facultyPost.setColor("ColorTestPost");

        Assertions.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyPost, String.class))
                .isNotNull();

        long idFacultyPost = facultyService.findIdByNameAndColor("FTestPost", "ColorTestPost");
        facultyService.remove(idFacultyPost);
    }

    @Test
    public void testPutFaculty() throws Exception {
        facultyTest.setName("FTestМ");
        restTemplate.put("http://localhost:" + port + "/faculty", facultyTest);
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + idFacultyTest, String.class))
                .contains("FTestМ");

        long idFacultyPut = facultyService.findIdByNameAndColor("FTestМ", "ColorTest");
        facultyService.remove(idFacultyPut);
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + idFacultyDel, String.class))
                .isNotNull();

        restTemplate.delete("http://localhost:" + port + "/faculty/" + idFacultyDel);

        try {
            restTemplate.getForObject("http://localhost:" + port + "/faculty/" + idFacultyDel, String.class);
        } catch (HttpClientErrorException ex) {
            Assertions.assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
