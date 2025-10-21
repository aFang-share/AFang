package com.example.afanguserbackend.controller.ai;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI聊天控制器
 * 提供AI对话接口，集成智谱AI服务
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController()
@RequestMapping("/public/ai")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AiController {

    private final OpenAiChatModel chatClient;

    /**
     * AI对话接口
     * 接收用户消息并返回AI生成的回复
     *
     * @param message 用户输入的消息内容
     * @return AI生成的回复内容
     */
    @GetMapping("/hello")
    public ResponseEntity<String> helloAi(@RequestParam  @Size(max = 1000) String message) {
        try {
            log.info("收到AI聊天请求，消息长度: {}", message.length());

            // 调用AI模型获取回复
            String response = chatClient.call(message);

            log.info("AI回复生成成功，回复长度: {}", response.length());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("AI聊天处理失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("抱歉，AI服务暂时不可用，请稍后再试。错误信息: " + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     * 用于验证AI服务是否正常工作
     *
     * @return 服务状态信息
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try {
            // 发送简单的测试消息
            String testResponse = chatClient.call("你好");
            return ResponseEntity.ok("AI服务正常 - " + (StringUtils.hasText(testResponse) ? "连接成功" : "连接异常"));
        } catch (Exception e) {
            log.error("AI服务健康检查失败: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("AI服务异常: " + e.getMessage());
        }
    }
}
