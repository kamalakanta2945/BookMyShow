package com.eventbook.movieservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig {
    // This class is used to enable Spring's caching mechanism.
    // Further customization, like setting TTL for caches, can be added here.
}