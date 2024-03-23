package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.Job;
import com.efjpr.rejob.domain.JobApplication;
import com.efjpr.rejob.repository.JobApplicationRepository;
import com.efjpr.rejob.repository.JobRepository;
import com.efjpr.rejob.utils.StopWords;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class JobRecommendationService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    private static double calculateJaccardSimilarity(String str1, String str2) {
        Set<String> set1 = tokenizeAndStem(removeStopwords(str1));
        Set<String> set2 = tokenizeAndStem(removeStopwords(str2));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return (double) intersection.size() / union.size();
    }

    private static Set<String> tokenizeAndStem(String text) {
        Set<String> stemmedTokens = new HashSet<>();

        try (BrazilianAnalyzer analyzer = new BrazilianAnalyzer()) {
            try (org.apache.lucene.analysis.TokenStream tokenStream = analyzer.tokenStream("fieldName", new StringReader(text))) {
                CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

                tokenStream.reset();

                while (tokenStream.incrementToken()) {
                    String stemmedToken = charTermAttribute.toString();
                    stemmedTokens.add(stemmedToken);
                }

                tokenStream.end();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return stemmedTokens;
    }

    private static String removeStopwords(String text) {
        Set<String> stopWords = StopWords.getPortugueseStopwords();
        for (String stopWord : stopWords) {
            text = text.replaceAll("\\b" + stopWord + "\\b", "");
        }

        return text.trim();
    }

    public List<Job> getBestJobs(Employee employee) {
        List<Job> appliedJobs = jobApplicationRepository.findAllByApplicant(employee).stream().map(JobApplication::getJob).toList();

        List<Job> allJobs = jobRepository.findOpenJobs();
        allJobs.removeAll(appliedJobs);

        String employeeExperienceAndSkills = preprocessText(employee.getProfessionalExperience() + " " + employee.getSkillsAndQualifications());

        Map<Job, Double> jobSimilarities = new HashMap<>();
        for (Job job : allJobs) {
            String jobExperienceAndResponsibilities = preprocessText(job.getRequiredExperience() + " " + job.getResponsibilities());
            double similarity = calculateJaccardSimilarity(employeeExperienceAndSkills, jobExperienceAndResponsibilities);
            jobSimilarities.put(job, similarity);
        }

        return jobSimilarities.entrySet().stream().sorted(Map.Entry.<Job, Double>comparingByValue().reversed()).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public double similarity(Employee employee, Job job) {
        String jobExperienceAndResponsibilities = preprocessText(job.getRequiredExperience() + " " + job.getResponsibilities());
        String employeeExperienceAndSkills = preprocessText(employee.getProfessionalExperience() + " " + employee.getSkillsAndQualifications());
        return calculateJaccardSimilarity(preprocessText(employeeExperienceAndSkills), preprocessText(jobExperienceAndResponsibilities));
    }

    private String preprocessText(String text) {
        return text.replace("\n", " ");
    }


}