package kr.irumdev.spring.developermaker.repository;

import kr.irumdev.spring.developermaker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperRepository
        extends JpaRepository<Developer, Long>{
    Optional<Developer> findByMemberId(String memberId);
}
