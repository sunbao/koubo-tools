package com.koubotools.scriptgenerator.controller;

import com.koubotools.scriptgenerator.service.TeleprompterService;
import com.koubotools.scriptgenerator.service.ScriptService;
import com.koubotools.scriptgenerator.model.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teleprompter")
public class TeleprompterController {
    
    @Autowired
    private TeleprompterService teleprompterService;
    
    @Autowired
    private ScriptService scriptService;
    
    /**
     * 将文案推送到提词器
     */
    @PostMapping("/push")
    public ResponseEntity<String> pushToTeleprompter(@RequestParam Long scriptId) {
        Script script = scriptService.getScriptById(scriptId).orElse(null);
        
        if (script == null) {
            return ResponseEntity.badRequest().body("Script not found");
        }
        
        boolean success = teleprompterService.pushToTeleprompter(script);
        
        if (success) {
            return ResponseEntity.ok("Script pushed to teleprompter successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to push script to teleprompter");
        }
    }
    
    /**
     * 获取提词器同步状态
     */
    @GetMapping("/status")
    public ResponseEntity<String> getTeleprompterStatus() {
        String status = teleprompterService.getTeleprompterStatus();
        return ResponseEntity.ok(status);
    }
    
    /**
     * 控制提词器播放/暂停等操作
     */
    @PostMapping("/control")
    public ResponseEntity<String> controlTeleprompter(@RequestParam String command) {
        boolean success = teleprompterService.controlTeleprompter(command);
        
        if (success) {
            return ResponseEntity.ok("Teleprompter command executed successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to execute teleprompter command");
        }
    }
    
    /**
     * 根据内容计算最佳语速
     */
    @PostMapping("/speed")
    public ResponseEntity<Integer> calculateOptimalSpeed(@RequestParam Long scriptId) {
        Script script = scriptService.getScriptById(scriptId).orElse(null);
        
        if (script == null) {
            return ResponseEntity.badRequest().body(-1);
        }
        
        int speed = teleprompterService.calculateOptimalSpeed(script.getContent());
        return ResponseEntity.ok(speed);
    }
    
    /**
     * 计算滚动持续时间
     */
    @PostMapping("/duration")
    public ResponseEntity<Double> calculateScrollDuration(@RequestParam Long scriptId, @RequestParam int speed) {
        Script script = scriptService.getScriptById(scriptId).orElse(null);
        
        if (script == null) {
            return ResponseEntity.badRequest().body(-1.0);
        }
        
        double duration = teleprompterService.calculateScrollDuration(script.getContent(), speed);
        return ResponseEntity.ok(duration);
    }
}