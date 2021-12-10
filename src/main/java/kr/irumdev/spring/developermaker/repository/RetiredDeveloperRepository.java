package kr.irumdev.spring.developermaker.repository;

import kr.irumdev.spring.developermaker.entity.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetiredDeveloperRepository
        extends JpaRepository<RetiredDeveloper, Long> {
}
