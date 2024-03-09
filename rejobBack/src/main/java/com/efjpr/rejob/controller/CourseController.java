package com.efjpr.rejob.controller;


import com.efjpr.rejob.domain.Course;
import com.efjpr.rejob.domain.Dto.CourseCreate;
import com.efjpr.rejob.domain.Dto.CourseResponse;
import com.efjpr.rejob.service.CourseService;
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

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseCreate course) {
        return new ResponseEntity<>(courseService.createCourse(course), HttpStatus.CREATED);
    }

    @GetMapping("course-list-by-collaborator/{collaboratorId}")
    public ResponseEntity<List<CourseResponse>> getAllCoursesByCollaboratorId(@PathVariable Long collaboratorId) {
        return new ResponseEntity<>(courseService.getAllCoursesByCollaboratorId(collaboratorId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return new ResponseEntity<>(courseService.getCourseById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody CourseCreate updatedCourse) {
        return new ResponseEntity<>(courseService.updateCourse(id, updatedCourse), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
