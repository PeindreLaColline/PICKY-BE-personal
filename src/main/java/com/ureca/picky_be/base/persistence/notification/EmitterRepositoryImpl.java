package com.ureca.picky_be.base.persistence.notification;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.*;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {
    // 동시성을 고려한 ConcurrentHashMap 사용

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return null;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterByUserId(String userId) {
        return emitters.entrySet().stream()
                .filter(enrty -> enrty.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheByUserId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    @Override
    public void deleteAllEmitterByUserId(String userId) {
        List<String> emitterIds = emitters.keySet().stream()
                .filter(key -> key.startsWith(userId))
                .toList();

        emitterIds.forEach(eventCache::remove);
    }

    @Override
    public void deleteAllEventCacheByUserId(String userId) {
        List<String> eventIds = eventCache.keySet().stream()
                .filter(key -> key.startsWith(userId))
                .toList();

        eventIds.forEach(eventCache::remove);
    }
}
