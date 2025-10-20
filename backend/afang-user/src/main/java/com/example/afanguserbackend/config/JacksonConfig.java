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
 * Jackson JSON配置类
 * 配置JSON序列化和反序列化规则，包括时间格式、精度处理等
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class JacksonConfig {

    /**
     * 自定义ObjectMapper Bean
     * 配置JSON序列化和反序列化的详细规则
     *
     * @param builder Jackson2对象映射构建器
     * @return 配置好的ObjectMapper实例
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

        // 注册 Java 8 时间模块，支持LocalDateTime和LocalDate的序列化
        JavaTimeModule timeModule = new JavaTimeModule();

        // 配置时间格式化规则
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 配置LocalDateTime的序列化和反序列化
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        // 配置LocalDate的序列化和反序列化
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        mapper.registerModule(timeModule);

        // 配置序列化策略
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 忽略null值
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳格式
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 忽略未知属性
        mapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为东八区

        return mapper;

    }
}