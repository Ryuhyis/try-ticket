package com.project.tryticket.domain.practice.repository;

import com.project.tryticket.domain.practice.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PracticeRepository extends JpaRepository<Practice, Long> {
}
