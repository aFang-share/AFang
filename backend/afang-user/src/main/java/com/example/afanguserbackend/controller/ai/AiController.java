package com.example.afanguserbackend.controller.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Spring AI 1.0.1 AI聊天控制器（修复SSE解析问题 + 优化依赖）
 */
@RestController
@RequestMapping("/public")
@Slf4j
public class AiController {

    // 核心修复1：OpenAiChatModel已实现StreamingChatModel，无需额外注入，减少冗余
    private final ChatClient chatClient;

    public AiController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    
    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;

    /**
     * 非流式聊天接口
     * 完整路径：http://localhost:端口/public/ai/chat
     */

    @GetMapping("/ai/generateStream")
    public String generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return this.chatClient.prompt().user(message).call().content();
    }

    /**
     * 流式聊天接口（返回OpenAPI格式）
     * 完整路径：http://localhost:端口/public/ai/stream
     */
    @GetMapping(value = "/ai/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam String message) {
        return chatClient.prompt(message)
                .stream()
                .chatResponse()
                .map(response -> {
                    // 提取响应内容
                    String content = response.getResult().getOutput().getText();
                    if (content == null) {
                        content = "";
                    }

                    // 安全提取metadata信息，添加空值检查
                    String messageId = "msg_" + System.currentTimeMillis();
                    String role = "ASSISTANT";
                    String finishReason = "";

                    try {
                        Object idObj = response.getResult().getOutput().getMetadata().get("id");
                        if (idObj != null) {
                            messageId = idObj.toString();
                        }
                    } catch (Exception e) {
                        log.debug("无法获取messageId，使用默认值: {}", e.getMessage());
                    }

                    try {
                        Object roleObj = response.getResult().getOutput().getMetadata().get("role");
                        if (roleObj != null) {
                            role = roleObj.toString();
                        }
                    } catch (Exception e) {
                        log.debug("无法获取role，使用默认值: {}", e.getMessage());
                    }

                    try {
                        Object finishReasonObj = response.getResult().getMetadata().get("finishReason");
                        if (finishReasonObj != null) {
                            finishReason = finishReasonObj.toString();
                        }
                    } catch (Exception e) {
                        log.debug("无法获取finishReason，使用默认值: {}", e.getMessage());
                    }

                    // 构建OpenAPI格式的响应
                    String openAiFormat = String.format("""
                        {
                            "id": "%s",
                            "object": "chat.completion.chunk",
                            "created": %d,
                            "model": "glm-4.5-flash",
                            "choices": [
                                {
                                    "index": 0,
                                    "delta": {
                                        "content": "%s"
                                    },
                                    "finish_reason": %s
                                }
                            ]
                        }
                        """,
                        messageId,
                        System.currentTimeMillis() / 1000,
                        content.replace("\"", "\\\"").replace("\n", "\\n"),
                        finishReason.isEmpty() ? "null" : "\"" + finishReason + "\""
                    );

                    return openAiFormat + "\n\n";
                });
    }

    /**
     * 直接调用智普AI原始SSE接口（绕过Spring AI包装）
     * 完整路径：http://localhost:端口/public/ai/raw-stream
     */
    @GetMapping(value = "/ai/raw-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> rawStreamChat(@RequestParam String message) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        String requestBody = String.format("""
            {
                "model": "glm-4.5-flash",
                "messages": [
                    {
                        "role": "user",
                        "content": "%s"
                    }
                ],
                "stream": true,
                "temperature": 0.9,
                "max_tokens": 2000
            }
            """, message.replace("\"", "\\\""));

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(chunk -> log.info("原始SSE数据块: {}", chunk))
                .filter(chunk -> !chunk.trim().isEmpty());
    }
}



