package com.koubotools.scriptgenerator.service;

import com.koubotools.scriptgenerator.model.Script;
import com.koubotools.scriptgenerator.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScriptService {
    
    @Autowired
    private ScriptRepository scriptRepository;
    
    public List<Script> getAllScripts() {
        return scriptRepository.findAll();
    }
    
    public Optional<Script> getScriptById(Long id) {
        return scriptRepository.findById(id);
    }
    
    public List<Script> getScriptsByPlatform(String platform) {
        return scriptRepository.findByPlatform(platform);
    }
    
    public List<Script> getScriptsByStatus(String status) {
        return scriptRepository.findByStatus(status);
    }
    
    public Script saveScript(Script script) {
        return scriptRepository.save(script);
    }
    
    public void deleteScript(Long id) {
        scriptRepository.deleteById(id);
    }
    
    /**
     * 更新文案状态
     */
    public Script updateScriptStatus(Long scriptId, String status) {
        Optional<Script> scriptOptional = scriptRepository.findById(scriptId);
        if (scriptOptional.isPresent()) {
            Script script = scriptOptional.get();
            script.setStatus(status);
            return scriptRepository.save(script);
        }
        return null;
    }
}