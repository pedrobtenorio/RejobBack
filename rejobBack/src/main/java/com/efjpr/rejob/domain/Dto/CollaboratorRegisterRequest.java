package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Enums.CollaboratorType;
import lombok.Data;

@Data
public class CollaboratorRegisterRequest {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String jobTitle;
    private CollaboratorType collaboratorType;
    private Long companyId;
}
