package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Enums.CollaboratorType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollaboratorRegisterRequest {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String jobTitle;
    private CollaboratorType collaboratorType;
    private Long companyId;
}
