package com.eventbook.eventservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig {
    // This class enables Spring's caching mechanism for the event service.
}