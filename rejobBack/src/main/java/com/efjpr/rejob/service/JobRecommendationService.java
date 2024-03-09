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


@RequiredArgsConstructor
@Service
public class JobRecommendationService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    public List<Job> getBestJobs(Employee employee) {
        List<Job> appliedJobs = jobApplicationRepository.findAllByApplicant(employee).stream().map(JobApplication::getJob).toList();

        List<Job> allJobs = jobRepository.findOpenJobs();
        allJobs.removeIf(appliedJobs::contains);

        Map<Job, Double> jobSimilarities = new HashMap<>();
        for (Job job : allJobs) {
            double similarity = calculateJaccardSimilarity(employee.getProfessionalExperience(), job.getRequiredExperience());
            jobSimilarities.put(job, similarity);
        }

        List<Job> recommendedJobs = new ArrayList<>(allJobs);
        recommendedJobs.sort((job1, job2) -> Double.compare(jobSimilarities.get(job2), jobSimilarities.get(job1)));

        return recommendedJobs.subList(0, Math.min(recommendedJobs.size(), 4));
    }

    public static double calculateJaccardSimilarity(String str1, String str2) {
        Set<String> set1 = tokenizeAndStem(removeStopwords(str1));
        Set<String> set2 = tokenizeAndStem(removeStopwords(str2));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return (double) intersection.size() / union.size();
    }

    public static Set<String> tokenizeAndStem(String text) {
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

    public static String removeStopwords(String text) {
        Set<String> stopWords = StopWords.getPortugueseStopwords();
        for (String stopWord : stopWords) {
            text = text.replaceAll("\\b" + stopWord + "\\b", "");
        }

        return text.trim();
    }



}