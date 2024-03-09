package com.efjpr.rejob.repository;

import com.efjpr.rejob.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j WHERE j.contactPerson.company.id = :companyId")
    List<Job> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT j FROM Job j WHERE j.jobStatus = com.efjpr.rejob.domain.Enums.JobStatus.ACTIVE " +
            "OR j.jobStatus = com.efjpr.rejob.domain.Enums.JobStatus.IN_PROGRESS")
    List<Job> findOpenJobs();

}
