package ru.hogwarts.school.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.Collections;

@RestController
public class InfoController {
    @Value("${server.port}")
    private String infoPort;

    @GetMapping("port") // GET https://localhost:8080/port
    @ManagedOperation(description = "порт, на котором запущено приложение")
    public ResponseEntity<String> getInfoPort() {
        return ResponseEntity.ok(infoPort);
    }
}
