package com.example.email_service.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

// @EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class security {
    // hasAnyRole("ADMIN", "STAFF")
    @Bean
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .formLogin(f -> f.disable())
                .csrf(crsf -> crsf.disable())
                .oauth2ResourceServer(oauth -> oauth.jwt(
                        Jwt -> Jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        authoritiesConverter.setAuthoritiesClaimName("roles");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    // @Bean
    // public RedisCacheManager RedisCache(RedisConnectionFactory factory) {
    // RedisCacheConfiguration cacheManager =
    // RedisCacheConfiguration.defaultCacheConfig()
    // .entryTtl(Duration.ofMinutes(1))
    // .disableCachingNullValues()
    // .serializeValuesWith(RedisSerializationContext.SerializationPair
    // .fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)));
    // return RedisCacheManager.builder(factory)
    // .cacheDefaults(cacheManager)
    // .build();
    // }

    // @Bean
    // public JedisConnectionFactory jedisConnectionFactory() {
    // return new JedisConnectionFactory();
    // }

    // @Bean
    // public RedisTemplate<String, Object> redisTemplate() {
    // RedisTemplate<String, Object> template = new RedisTemplate<>();
    // template.setConnectionFactory(jedisConnectionFactory());
    // Jackson2JsonRedisSerializer<Object> serializer = new
    // Jackson2JsonRedisSerializer<>(Object.class);
    // template.setDefaultSerializer(serializer);
    // return template;
    // }

}
