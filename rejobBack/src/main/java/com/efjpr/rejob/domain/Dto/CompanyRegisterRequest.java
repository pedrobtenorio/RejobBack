package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Enums.CompanyType;
import com.efjpr.rejob.domain.Location;
import lombok.Data;

@Data
public class CompanyRegisterRequest {

    private String cnpj;
    private String name;
    private String businessActivity;
    private int numberOfEmployees;
    private Location headquarters;
    private String phone;
    private String institutionalDescription;
    private String email;
    private String password;
    private CompanyType companyType;
}
