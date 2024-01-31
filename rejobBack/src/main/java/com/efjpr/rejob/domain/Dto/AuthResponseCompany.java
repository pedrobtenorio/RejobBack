package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Company;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseCompany {
    String token;
    Company company;
}
