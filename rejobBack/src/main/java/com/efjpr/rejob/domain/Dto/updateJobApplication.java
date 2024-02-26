package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Enums.ApplicationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class updateJobApplication {
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String feedback;
}
