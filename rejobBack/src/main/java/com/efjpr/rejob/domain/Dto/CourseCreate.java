package com.efjpr.rejob.domain.Dto;

import lombok.Data;

@Data
public class CourseCreate {

    private Long id;
    private Long contactPersonId;
    private String courseTitle;
    private String platform;
    private String link;
    private String description;
    private String duration;
}
