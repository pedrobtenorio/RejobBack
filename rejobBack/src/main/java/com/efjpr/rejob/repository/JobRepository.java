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

    @Query("SELECT j FROM Job j " +
            "WHERE (:name is null or lower(j.jobTitle) like %:name%) " +
            "AND (:categories is null or lower(j.categories) like %:categories%) " +
            "AND (:minSalary is null or j.salaryRange.salaryRangeMin >= :minSalary) " +
            "AND (:maxSalary is null or j.salaryRange.salaryRangeMax <= :maxSalary) " +
            "AND (:state is null or lower(j.companyLocation.state) like %:state%)")
    List<Job> searchJobs(@Param("name") String name,
                         @Param("categories") String categories,
                         @Param("minSalary") Float minSalary,
                         @Param("maxSalary") Float maxSalary,
                         @Param("state") String state);

}
