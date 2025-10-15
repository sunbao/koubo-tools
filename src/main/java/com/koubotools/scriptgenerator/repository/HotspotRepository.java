package com.koubotools.scriptgenerator.repository;

import com.koubotools.scriptgenerator.model.Hotspot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotspotRepository extends JpaRepository<Hotspot, Long> {
    List<Hotspot> findByPlatform(String platform);
    List<Hotspot> findByType(String type);
    List<Hotspot> findByPlatformAndType(String platform, String type);
}