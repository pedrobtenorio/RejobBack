package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Enums.CollaboratorType;
import com.efjpr.rejob.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollaboratorGetRequest {

    private User user;
    private String jobTitle;
    private CollaboratorType collaboratorType;
    private Long companyId;
    private Long collaboratorId;
}
