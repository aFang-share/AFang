package com.example.afanguserbackend.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Spring MVC Json 配置
 */
//@JsonComponent
    @Configuration
public class JacksonConfig {

    /**
     * 添加 Long 转 json 精度丢失的配置
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(Long.class, ToStringSerializer.instance);
//        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        objectMapper.registerModule(module);
//        return objectMapper;


        ObjectMapper mapper = new ObjectMapper();

        // 注册 Java 8 时间模块
        JavaTimeModule timeModule = new JavaTimeModule();

        // 配置 LocalDateTime 格式化
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dtf));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dtf));

        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(df));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(df));

        mapper.registerModule(timeModule);

        // 通用配置
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        return mapper;

    }
}