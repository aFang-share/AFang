package com.example.afanguserbackend.utils;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信验证码发送工具类。
 * <p>
 * 该工具类封装了短信验证码发送功能，通过阿里云市场国阳云短信服务实现。
 * 支持发送包含验证码和有效期的短信通知。
 * <p>
 * 注意：该实现依赖于第三方短信服务，需要配置相应的APPCode。
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
public class PhoneUtil {

    /**
     * 发送短信验证码。
     * <p>
     * 通过国阳云短信服务发送包含验证码和有效期的短信。
     * 短信内容使用预定义模板，验证码和有效时间通过参数传递。
     * <p>
     * 配置信息：
     * <ul>
     *   <li>服务商：国阳云短信服务</li>
     *   <li>API地址：https://gyytz.market.alicloudapi.com</li>
     *   <li>短信签名ID：2e65b1bb3d054466b82f0c9d125465e2</li>
     *   <li>模板ID：908e94ccf08b4476ba6c876d13f084ad</li>
     * </ul>
     *
     * @param phone  接收短信的手机号码
     * @param code   6位数字验证码
     * @param minute 验证码有效时间（分钟）
     * @throws RuntimeException 当短信发送失败时抛出
     */
    public static void sendPhoneCode(String phone, String code, String minute) {
        // 构建短信模板参数
        String msg = "**code**:" + code + ",**minute**:" + minute;

        // 国阳云短信服务配置
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";

        // 应用代码，用于API认证
        String appcode = "35baf961ca9c4447aecbdd58ddcdfb03";

        // 构建请求头
        Map<String, String> headers = new HashMap<String, String>();
        // 格式：Authorization:APPCODE appcode（中间是英文空格）
        headers.put("Authorization", "APPCODE " + appcode);

        // 构建查询参数
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", msg);

        // 短信签名和模板配置
        // smsSignId（短信签名）和templateId（短信模板）需要登录国阳云控制台申请
        // 参考文档：http://help.guoyangyun.com/Problem/Qm.html
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");

        // 请求体（此接口使用查询参数，请求体为空）
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            // 发送HTTP请求到短信服务API
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            // 打印响应信息（用于调试，生产环境建议使用日志）
            System.out.println("短信发送响应: " + response.toString());

            // 获取响应体的完整内容（如需处理响应结果，可取消注释）
            // System.out.println(EntityUtils.toString(response.getEntity()));

        } catch (Exception e) {
            // 记录异常信息（生产环境建议使用日志框架）
            e.printStackTrace();
            throw new RuntimeException("短信发送失败: " + e.getMessage(), e);
        }
    }

}
