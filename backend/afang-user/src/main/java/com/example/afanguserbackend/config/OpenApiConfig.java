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

/**
 * OpenAPI配置类
 * 配置Swagger/OpenAPI文档生成，提供API文档界面
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class OpenApiConfig {

    /**
     * 自定义OpenAPI配置
     * 配置API文档的基本信息，如标题、版本、描述等
     *
     * @return 配置好的OpenAPI实例
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AFang用户管理 API")
                        .version("1.0")
                        .description("AFang用户管理系统API文档，提供用户注册、登录、信息管理等功能")
                        .contact(new Contact()
                                .name("AFang开发团队")
                                .email("dev@afang.com")));
    }

    /**
     * OpenAPI自定义器
     * 用于自定义API文档的展示格式和内容
     *
     * @return OpenAPI自定义器实例
     */
    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            // 创建tag名称到描述的映射
            Map<String, String> tagDescriptionMap = openApi.getTags() != null ?
                    openApi.getTags().stream()
                            .filter(tag -> tag.getDescription() != null)
                            .collect(Collectors.toMap(Tag::getName, Tag::getDescription)) :
                    Map.of();

            // 处理每个路径的操作，优化tag显示
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
