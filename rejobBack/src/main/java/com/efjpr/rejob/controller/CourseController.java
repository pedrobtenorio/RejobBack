package com.efjpr.rejob.controller;


import com.efjpr.rejob.domain.Course;
import com.efjpr.rejob.domain.Dto.CourseCreate;
import com.efjpr.rejob.domain.Dto.CourseResponse;
import com.efjpr.rejob.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(description = "Cria um curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador com esse id não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseCreate course) {
        return new ResponseEntity<>(courseService.createCourse(course), HttpStatus.CREATED);
    }

    @Operation(description = "Retorna todos os cursos associados a um colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos retornados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("course-list-by-collaborator/{collaboratorId}")
    public ResponseEntity<List<CourseResponse>> getAllCoursesByCollaboratorId(@PathVariable Long collaboratorId) {
        return new ResponseEntity<>(courseService.getAllCoursesByCollaboratorId(collaboratorId), HttpStatus.OK);
    }

    @Operation(description = "Retorna todos os cursos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos retornados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping()
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @Operation(description = "Retorna curso pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum curso com esse id foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return new ResponseEntity<>(courseService.getCourseById(id), HttpStatus.OK);
    }

    @Operation(description = "Altera informações de um curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações atualizadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum curso com esse id foi encontrado / Colaborador com esse id não encontrado"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody CourseCreate updatedCourse) {
        return new ResponseEntity<>(courseService.updateCourse(id, updatedCourse), HttpStatus.OK);
    }

    @Operation(description = "Deleta um curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum curso com esse id foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
