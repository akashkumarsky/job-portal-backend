package com.Board.job.service;

import com.Board.job.Repository.JobRepository;
import com.Board.job.Repository.UserRepository;
import com.Board.job.dto.JobRequest;
import com.Board.job.entity.Job;
import com.Board.job.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
    }

    public Job createJob(JobRequest request) {
        User employer = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Job job = new Job();
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setLocation(request.location());
        job.setSalary(request.salary());
        job.setCompany(request.company());
        job.setUser(employer);

        return jobRepository.save(job);
    }

    public Job updateJob(Long id, JobRequest request) {
        Job job = getJobById(id);
        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!job.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can only update your own job");
        }

        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setLocation(request.location());
        job.setSalary(request.salary());
        job.setCompany(request.company());

        return jobRepository.save(job);
    }

    public void deleteJob (Long id){
        Job job = getJobById(id);
        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!job.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can only delete your own job");
        }
        jobRepository.delete(job);
    }

}
