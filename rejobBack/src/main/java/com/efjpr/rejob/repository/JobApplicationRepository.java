package com.efjpr.rejob.repository;

import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.Job;
import com.efjpr.rejob.domain.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findAllByApplicant(Employee applicant);

    List<JobApplication> findAllByJobId(Long jobId);

    JobApplication findByApplicantAndJob(Employee applicant, Job job);

    boolean existsByApplicantAndJob(Employee applicant, Job job);
}
