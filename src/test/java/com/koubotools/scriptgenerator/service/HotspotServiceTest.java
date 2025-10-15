package com.koubotools.scriptgenerator.service;

import com.koubotools.scriptgenerator.model.Hotspot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotspotServiceTest {

    @Autowired
    private HotspotService hotspotService;

    @Test
    void testFetchHotspotsFromExternalAPI() {
        // 获取热点数据
        List<Hotspot> hotspots = hotspotService.fetchHotspotsFromExternalAPI();
        
        // 验证结果
        assertNotNull(hotspots);
        assertFalse(hotspots.isEmpty());
        assertTrue(hotspots.size() > 0);
        
        // 验证热点数据的基本属性
        Hotspot firstHotspot = hotspots.get(0);
        assertNotNull(firstHotspot.getTitle());
        assertNotNull(firstHotspot.getContent());
        assertNotNull(firstHotspot.getPlatform());
        assertTrue(firstHotspot.getHeatIndex() > 0);
    }
}