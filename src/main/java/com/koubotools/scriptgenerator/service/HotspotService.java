package com.koubotools.scriptgenerator.service;

import com.koubotools.scriptgenerator.model.Hotspot;
import com.koubotools.scriptgenerator.repository.HotspotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class HotspotService {
    
    @Autowired
    private HotspotRepository hotspotRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${hotspot.api.url:https://api.example.com/hotspots}")
    private String hotspotApiUrl;
    
    @Cacheable(value = "hotspots", key = "'all'")
    public List<Hotspot> getAllHotspots() {
        return hotspotRepository.findAll();
    }
    
    @Cacheable(value = "hotspots", key = "#id")
    public Optional<Hotspot> getHotspotById(Long id) {
        return hotspotRepository.findById(id);
    }
    
    @Cacheable(value = "hotspots", key = "'platform:' + #platform")
    public List<Hotspot> getHotspotsByPlatform(String platform) {
        return hotspotRepository.findByPlatform(platform);
    }
    
    @Cacheable(value = "hotspots", key = "'type:' + #type")
    public List<Hotspot> getHotspotsByType(String type) {
        return hotspotRepository.findByType(type);
    }
    
    @CacheEvict(value = "hotspots", allEntries = true)
    public Hotspot saveHotspot(Hotspot hotspot) {
        return hotspotRepository.save(hotspot);
    }
    
    @CacheEvict(value = "hotspots", allEntries = true)
    public void deleteHotspot(Long id) {
        hotspotRepository.deleteById(id);
    }
    
    /**
     * 从外部API获取热点数据
     * 实际实现中需要调用真实的热点数据服务接口
     */
    public List<Hotspot> fetchHotspotsFromExternalAPI() {
        try {
            // 这里应该实现调用外部热点数据服务的逻辑
            // 例如：HotspotApiResponse response = restTemplate.getForObject(hotspotApiUrl, HotspotApiResponse.class);
            // 暂时返回空列表，实际项目中需要实现真实的API调用
            
            // 模拟一些热点数据用于演示
            Hotspot hotspot1 = new Hotspot("AI技术发展趋势", "AI技术在各行业的应用越来越广泛", "抖音", 95.5);
            hotspot1.setType("科技");
            hotspot1.setTags(List.of("AI", "科技", "未来"));
            
            Hotspot hotspot2 = new Hotspot("夏日美食推荐", "炎炎夏日，来点清爽美食", "快手", 88.2);
            hotspot2.setType("美食");
            hotspot2.setTags(List.of("美食", "夏日", "清爽"));
            
            Hotspot hotspot3 = new Hotspot("旅行攻略分享", "国内热门旅游景点推荐", "抖音", 92.1);
            hotspot3.setType("旅游");
            hotspot3.setTags(List.of("旅行", "攻略", "景点"));
            
            return List.of(hotspot1, hotspot2, hotspot3);
        } catch (Exception e) {
            // 记录日志
            System.err.println("获取热点数据失败: " + e.getMessage());
            // 返回空列表或缓存数据
            return List.of();
        }
    }
    
    /**
     * 定期更新热点数据
     */
    @CacheEvict(value = "hotspots", allEntries = true)
    public void updateHotspotsPeriodically() {
        List<Hotspot> hotspots = fetchHotspotsFromExternalAPI();
        for (Hotspot hotspot : hotspots) {
            // 保存到数据库
            saveHotspot(hotspot);
        }
    }
}