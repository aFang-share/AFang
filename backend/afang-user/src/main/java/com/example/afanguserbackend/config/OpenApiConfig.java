package com.example.afanguserbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("File Transfers API")
                        .version("1.0")
                        .description("API documentation for File Transfers application")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@example.com")));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            // 创建tag名称到描述的映射
            Map<String, String> tagDescriptionMap = openApi.getTags() != null ?
                    openApi.getTags().stream()
                            .filter(tag -> tag.getDescription() != null)
                            .collect(Collectors.toMap(Tag::getName, Tag::getDescription)) :
                    Map.of();

            // 处理每个路径的操作
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, pathItem) ->
                        pathItem.readOperations().forEach(operation -> {
                            List<String> tags = operation.getTags();
                            if (tags != null && !tags.isEmpty()) {
                                // 将tags中的每个tag名称替换为对应的描述信息
                                List<String> updatedTags = tags.stream()
                                        .map(tagName -> tagDescriptionMap.getOrDefault(tagName, tagName))
                                        .collect(Collectors.toList());
                                operation.setTags(updatedTags);
                            }
                        })
                );
            }
        };
    }
}
