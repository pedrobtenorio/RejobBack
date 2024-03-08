package com.efjpr.rejob.controller;


import com.efjpr.rejob.domain.Course;
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

    @GetMapping
    public ResponseEntity<List<CourseResponse>>
}
