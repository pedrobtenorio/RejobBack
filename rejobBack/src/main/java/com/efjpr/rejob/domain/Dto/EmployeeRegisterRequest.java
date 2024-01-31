package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Enums.EducationLevel;
import com.efjpr.rejob.domain.Enums.SentenceRegime;
import com.efjpr.rejob.domain.Location;
import lombok.Data;

@Data
public class EmployeeRegisterRequest {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    private String cpf;
    private String prisonCode;
    private EducationLevel educationLevel;
    private String dateOfBirth;
    private Location residenceLocation;
    private SentenceRegime sentenceRegime;
    private String professionalExperience;
    private String areasOfInterest;
    private String skillsAndQualifications;
    private String educationalHistory;
}
