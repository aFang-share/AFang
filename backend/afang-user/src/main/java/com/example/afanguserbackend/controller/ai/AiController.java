package com.example.afanguserbackend.controller.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Spring AI 1.0.1 AI聊天控制器（修复SSE解析问题 + 优化依赖）
 */
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    // 核心修复1：OpenAiChatModel已实现StreamingChatModel，无需额外注入，减少冗余
    private final OpenAiChatModel chatModel;

    /**
     * 非流式聊天接口
     * 完整路径：http://localhost:端口/public/ai/chat
     */
    @GetMapping("/ai/chat")
    public ResponseEntity<String> chat(@RequestParam String message) {
        if (!StringUtils.hasText(message)) {
            return ResponseEntity.badRequest().body("消息不能为空");
        }
        try {
            Prompt prompt = new Prompt(new UserMessage(message));
            ChatResponse response = chatModel.call(prompt);
            String content = getResponseContent(response);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            log.error("非流式聊天失败", e);
            return ResponseEntity.internalServerError().body("AI服务暂时不可用");
        }
    }

    /**
     * 流式聊天接口（核心修复：确保SSE格式标准，Apifox可解析）
     * 完整路径：http://localhost:端口/public/ai/stream
     */
    @GetMapping(value = "/ai/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(
            @RequestParam String message
    ) {

        // 2. 校验入参，避免空消息
        if (!StringUtils.hasText(message)) {
            String errorChunk = buildOpenAiJsonChunk("消息不能为空", "stop", generateStreamId());
            return Flux.just(errorChunk);
        }

        // 3. 生成唯一Stream ID（每次请求独立）
        String streamId = generateStreamId();

        // 4. 核心修复3：用Prompt封装消息（避免直接传String导致的原始流格式问题）
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt) // 用OpenAiChatModel的stream(Prompt)方法，流更规范
                .map(chatResponse -> extractChunkContent(chatResponse)) // 安全提取内容
                .filter(StringUtils::hasText) // 过滤空内容
                .map(content -> buildOpenAiJsonChunk(content, null, streamId)) // 构建标准JSON块
                .concatWith(Flux.just(buildOpenAiJsonChunk("", "stop", streamId))) // 结束标记
                .onErrorResume(e -> {
                    log.error("流式聊天失败", e);
                    String errorJson = buildOpenAiErrorJson("流式响应异常，请稍后重试");
                    return Flux.just("data: " + errorJson + "\n\n");
                });
    }

    /**
     * 工具方法1：生成OpenAI风格的Stream ID
     */
    private String generateStreamId() {
        // 格式：chatcmpl- + 24位随机字符串（匹配OpenAI规范）
        return "chatcmpl-" + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }

    /**
     * 工具方法2：从ChatResponse提取单块流式内容（避免直接操作String流的格式问题）
     */
    private String extractChunkContent(ChatResponse chatResponse) {
        if (chatResponse == null || chatResponse.getResults().isEmpty()) {
            return "";
        }
        var firstResult = chatResponse.getResults().get(0);
        if (firstResult == null || firstResult.getOutput() == null) {
            return "";
        }
        // 安全提取文本，避免null
        return StringUtils.hasText(firstResult.getOutput().getText())
                ? firstResult.getOutput().getText()
                : "";
    }

    /**
     * 工具方法3：构建标准OpenAI JSON块（确保SSE格式严格）
     */
    private String buildOpenAiJsonChunk(String content, String finishReason, String streamId) {
        long timestamp = System.currentTimeMillis() / 1000;
        String deltaJson = content.isEmpty() ? "{}" : "{\"content\":\"" + escapeJson(content) + "\"}";
        String finishReasonStr = finishReason == null ? "null" : "\"" + finishReason + "\"";

        String json = String.format(
                "{\"id\":\"%s\",\"object\":\"chat.completion.chunk\",\"created\":%d,\"model\":\"gpt-3.5-turbo-0613\",\"choices\":[{\"delta\":%s,\"index\":0,\"finish_reason\":%s}]}",
                streamId, timestamp, deltaJson, finishReasonStr
        );

        return json;
    }

    /**
     * 工具方法4：构建OpenAI风格错误JSON
     */
    private String buildOpenAiErrorJson(String errorMsg) {
        return String.format(
                "{\"error\":{\"message\":\"%s\",\"type\":\"stream_error\",\"param\":null,\"code\":null}}",
                escapeJson(errorMsg)
        );
    }

    /**
     * 工具方法5：JSON特殊字符转义（避免引号、换行导致的JSON格式错误）
     */
    private String escapeJson(String content) {
        if (content == null) return "";
        return content.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t"); // 补充转义制表符，避免格式问题
    }

    /**
     * 工具方法6：从非流式ChatResponse提取内容
     */
    private String getResponseContent(ChatResponse response) {
        if (response == null || response.getResults().isEmpty()) {
            return "无响应";
        }
        var firstResult = response.getResults().get(0);
        return firstResult != null && firstResult.getOutput() != null
                ? firstResult.getOutput().getText()
                : "无响应";
    }
}