package com.Board.job.Repository;

import com.Board.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface jobRepository extends JpaRepository<Job, Long> {

}
