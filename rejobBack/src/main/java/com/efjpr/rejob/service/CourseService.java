package com.efjpr.rejob.service;


import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Course;
import com.efjpr.rejob.domain.Dto.CourseCreate;
import com.efjpr.rejob.domain.Dto.CourseResponse;
import com.efjpr.rejob.repository.CollaboratorRepository;
import com.efjpr.rejob.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CollaboratorRepository collaboratorRepository;

    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());
    }

    public List<CourseResponse> getAllCoursesByCollaboratorId(Long collaboratorId) {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .filter(course -> course.getContactPerson().getId().equals(collaboratorId))
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());
    }

    public Course createCourse(CourseCreate coursePayload) {
        Collaborator contactPerson = collaboratorRepository.findById(coursePayload.getContactPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator with ID " + coursePayload.getContactPersonId() + " not found"));

        Course course = buildCourseFromPayload(coursePayload, contactPerson);

        return courseRepository.save(course);
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with ID " + id + " not found"));

        // Convert Course to CourseResponse
        return convertToCourseResponse(course);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with ID " + id + " not found"));
    }

    public Course updateCourse(Long id, CourseCreate updatedCourse) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with ID " + id + " not found") {
                });
        Collaborator contactPerson = collaboratorRepository.findById(updatedCourse.getContactPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator with ID " + updatedCourse.getContactPersonId() + " not found"));

        validateAndApplyUpdates(existingCourse, updatedCourse, contactPerson);
        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(Long id) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with ID " + id + " not found"));
        courseRepository.delete(existingCourse);
    }

    private void validateAndApplyUpdates(Course existingCourse, CourseCreate updatedCourse, Collaborator collaborator) {
        existingCourse.setContactPerson(collaborator);
        existingCourse.setCourseTitle(updatedCourse.getCourseTitle());
        existingCourse.setPlatform(updatedCourse.getPlatform());
        existingCourse.setLink(updatedCourse.getLink());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setDuration(updatedCourse.getDuration());
    }

    private Course buildCourseFromPayload(CourseCreate coursePayload, Collaborator contactPerson) {
        return Course.builder()
                .contactPerson(contactPerson)
                .courseTitle(coursePayload.getCourseTitle())
                .platform(coursePayload.getPlatform())
                .link(coursePayload.getLink())
                .description(coursePayload.getDescription())
                .duration(coursePayload.getDuration())
                .build();
    }

    private CourseResponse convertToCourseResponse(Course course) {
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setContactPerson(course.getContactPerson());
        courseResponse.setCourseTitle(course.getCourseTitle());
        courseResponse.setPlatform(course.getPlatform());
        courseResponse.setLink(course.getLink());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setDuration(course.getDuration());

        return courseResponse;
    }

}
