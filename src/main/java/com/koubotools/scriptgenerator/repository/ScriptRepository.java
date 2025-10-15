package com.koubotools.scriptgenerator.repository;

import com.koubotools.scriptgenerator.model.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    List<Script> findByPlatform(String platform);
    List<Script> findByStatus(String status);
    List<Script> findByPlatformAndStatus(String platform, String status);
}