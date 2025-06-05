package com.wise.transdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@ComponentScan(basePackages = "com.wise.transdemo")
public class JacksonConfig {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // 注册Java 8日期时间模块
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(objectMapper());
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // 自定义 LocalDateTime 序列化格式
            builder.serializerByType(LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));

            // 自定义 LocalDateTime 反序列化格式
            builder.deserializerByType(LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        };
    }
}
