package com.koubotools.scriptgenerator.controller;

import com.koubotools.scriptgenerator.model.Hotspot;
import com.koubotools.scriptgenerator.service.HotspotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotspots")
public class HotspotController {
    
    @Autowired
    private HotspotService hotspotService;
    
    @GetMapping
    public ResponseEntity<List<Hotspot>> getAllHotspots() {
        List<Hotspot> hotspots = hotspotService.getAllHotspots();
        return ResponseEntity.ok(hotspots);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Hotspot> getHotspotById(@PathVariable Long id) {
        return hotspotService.getHotspotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/platform/{platform}")
    public ResponseEntity<List<Hotspot>> getHotspotsByPlatform(@PathVariable String platform) {
        List<Hotspot> hotspots = hotspotService.getHotspotsByPlatform(platform);
        return ResponseEntity.ok(hotspots);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Hotspot>> getHotspotsByType(@PathVariable String type) {
        List<Hotspot> hotspots = hotspotService.getHotspotsByType(type);
        return ResponseEntity.ok(hotspots);
    }
    
    @PostMapping
    public ResponseEntity<Hotspot> createHotspot(@RequestBody Hotspot hotspot) {
        Hotspot savedHotspot = hotspotService.saveHotspot(hotspot);
        return ResponseEntity.ok(savedHotspot);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Hotspot> updateHotspot(@PathVariable Long id, @RequestBody Hotspot hotspot) {
        hotspot.setHotId(id);
        Hotspot updatedHotspot = hotspotService.saveHotspot(hotspot);
        return ResponseEntity.ok(updatedHotspot);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotspot(@PathVariable Long id) {
        hotspotService.deleteHotspot(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/fetch")
    public ResponseEntity<List<Hotspot>> fetchHotspotsFromExternalAPI() {
        List<Hotspot> hotspots = hotspotService.fetchHotspotsFromExternalAPI();
        return ResponseEntity.ok(hotspots);
    }
}