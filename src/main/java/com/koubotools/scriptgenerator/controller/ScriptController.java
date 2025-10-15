package com.koubotools.scriptgenerator.controller;

import com.koubotools.scriptgenerator.model.Script;
import com.koubotools.scriptgenerator.service.AiScriptGenerationService;
import com.koubotools.scriptgenerator.service.ScriptService;
import com.koubotools.scriptgenerator.service.HotspotService;
import com.koubotools.scriptgenerator.service.UserService;
import com.koubotools.scriptgenerator.model.User;
import com.koubotools.scriptgenerator.model.Hotspot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/scripts")
public class ScriptController {
    
    @Autowired
    private ScriptService scriptService;
    
    @Autowired
    private AiScriptGenerationService aiScriptGenerationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private HotspotService hotspotService;
    
    @GetMapping
    public ResponseEntity<List<Script>> getAllScripts() {
        List<Script> scripts = scriptService.getAllScripts();
        return ResponseEntity.ok(scripts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Script> getScriptById(@PathVariable Long id) {
        return scriptService.getScriptById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/platform/{platform}")
    public ResponseEntity<List<Script>> getScriptsByPlatform(@PathVariable String platform) {
        List<Script> scripts = scriptService.getScriptsByPlatform(platform);
        return ResponseEntity.ok(scripts);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Script>> getScriptsByStatus(@PathVariable String status) {
        List<Script> scripts = scriptService.getScriptsByStatus(status);
        return ResponseEntity.ok(scripts);
    }
    
    @PostMapping
    public ResponseEntity<Script> createScript(@RequestBody Script script) {
        Script savedScript = scriptService.saveScript(script);
        return ResponseEntity.ok(savedScript);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Script> updateScript(@PathVariable Long id, @RequestBody Script script) {
        script.setScriptId(id);
        Script updatedScript = scriptService.saveScript(script);
        return ResponseEntity.ok(updatedScript);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScript(@PathVariable Long id) {
        scriptService.deleteScript(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Script> updateScriptStatus(@PathVariable Long id, @RequestParam String status) {
        Script script = scriptService.updateScriptStatus(id, status);
        if (script != null) {
            return ResponseEntity.ok(script);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据热点和用户配置生成AI文案
     */
    @PostMapping("/generate")
    public ResponseEntity<Script> generateScript(
            @RequestParam Long userId,
            @RequestParam Long hotspotId) {
        
        // 获取用户和热点信息
        User user = userService.getUserById(userId).orElse(null);
        Hotspot hotspot = hotspotService.getHotspotById(hotspotId).orElse(null);
        
        if (user == null || hotspot == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 生成文案
        Script script = aiScriptGenerationService.generateScript(user, hotspot);
        
        // 保存文案
        Script savedScript = scriptService.saveScript(script);
        
        return ResponseEntity.ok(savedScript);
    }
}