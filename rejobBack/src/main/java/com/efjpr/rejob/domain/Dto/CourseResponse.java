package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Collaborator;
import lombok.Data;

@Data
public class CourseResponse {

    private Long id;
    private Collaborator contactPerson;
    private String courseTitle;
    private String platform;
    private String link;
    private String description;
    private String duration;
}
