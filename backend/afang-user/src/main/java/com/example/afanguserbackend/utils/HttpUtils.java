package com.example.afanguserbackend.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP工具类，提供HTTP请求的封装方法。
 * <p>
 * 该工具类基于Apache HttpClient实现，提供了常用的HTTP操作方法，包括：
 * <ul>
 *   <li>GET请求</li>
 *   <li>POST请求（支持表单数据、字符串、字节数组）</li>
 *   <li>PUT请求（支持字符串、字节数组）</li>
 *   <li>DELETE请求</li>
 *   <li>HTTPS支持（自动SSL配置）</li>
 * </ul>
 * <p>
 * 支持自定义请求头、查询参数和请求体，自动处理URL编码和SSL证书验证。
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
public class HttpUtils {

	/**
	 * 执行HTTP GET请求。
	 * <p>
	 * 发送GET请求到指定主机和路径，支持自定义请求头和查询参数。
	 * 自动处理HTTPS连接的SSL配置。
	 *
	 * @param host    请求主机地址，如：https://api.example.com
	 * @param path    请求路径，如：/users
	 * @param method  HTTP方法，固定为"GET"
	 * @param headers 请求头映射，可以为null
	 * @param querys  查询参数映射，可以为null
	 * @return HTTP响应对象
	 * @throws Exception 当请求过程中发生错误时抛出
	 */
	public static HttpResponse doGet(String host, String path, String method,
			Map<String, String> headers,
			Map<String, String> querys)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpGet request = new HttpGet(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

	/**
	 * 执行HTTP POST请求（表单数据）。
	 * <p>
	 * 发送POST请求，请求体为表单格式（application/x-www-form-urlencoded）。
	 * 自动进行URL编码和字符集设置。
	 *
	 * @param host    请求主机地址，如：https://api.example.com
	 * @param path    请求路径，如：/users
	 * @param method  HTTP方法，固定为"POST"
	 * @param headers 请求头映射，可以为null
	 * @param querys  查询参数映射，可以为null
	 * @param bodys   表单数据映射，可以为null
	 * @return HTTP响应对象
	 * @throws Exception 当请求过程中发生错误时抛出
	 */
	public static HttpResponse doPost(String host, String path, String method,
			Map<String, String> headers,
			Map<String, String> querys,
			Map<String, String> bodys)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

	/**
	 * 执行HTTP POST请求（字符串内容）。
	 * <p>
	 * 发送POST请求，请求体为字符串格式，适用于发送JSON、XML等文本数据。
	 * 使用UTF-8编码。
	 *
	 * @param host    请求主机地址，如：https://api.example.com
	 * @param path    请求路径，如：/users
	 * @param method  HTTP方法，固定为"POST"
	 * @param headers 请求头映射，可以为null
	 * @param querys  查询参数映射，可以为null
	 * @param body    请求体字符串，可以为null或空字符串
	 * @return HTTP响应对象
	 * @throws Exception 当请求过程中发生错误时抛出
	 */
	public static HttpResponse doPost(String host, String path, String method,
			Map<String, String> headers,
			Map<String, String> querys,
			String body)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
        	request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

	/**
	 * 执行HTTP POST请求（字节数组）。
	 * <p>
	 * 发送POST请求，请求体为字节数组格式，适用于上传文件、图片等二进制数据。
	 *
	 * @param host    请求主机地址，如：https://api.example.com
	 * @param path    请求路径，如：/upload
	 * @param method  HTTP方法，固定为"POST"
	 * @param headers 请求头映射，可以为null
	 * @param querys  查询参数映射，可以为null
	 * @param body    请求体字节数组，可以为null
	 * @return HTTP响应对象
	 * @throws Exception 当请求过程中发生错误时抛出
	 */
	public static HttpResponse doPost(String host, String path, String method,
			Map<String, String> headers,
			Map<String, String> querys,
			byte[] body)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
        	request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

	/**
	 * 执行HTTP PUT请求（字符串内容）。
	 * <p>
	 * 发送PUT请求，请求体为字符串格式，适用于更新资源的操作。
	 * 使用UTF-8编码。
	 *
	 * @param host    请求主机地址，如：https://api.example.com
	 * @param path    请求路径，如：/users/123
	 * @param method  HTTP方法，固定为"PUT"
	 * @param headers 请求头映射，可以为null
	 * @param querys  查询参数映射，可以为null
	 * @param body    请求体字符串，可以为null或空字符串
	 * @return HTTP响应对象
	 * @throws Exception 当请求过程中发生错误时抛出
	 */
	public static HttpResponse doPut(String host, String path, String method,
			Map<String, String> headers,
			Map<String, String> querys,
			String body)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
        	request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

	/**
	 * 执行HTTP PUT请求（字节数组）。
	 * <p>
	 * 发送PUT请求，请求体为字节数组格式，适用于上传文件、图片等二进制数据来更新资源。
	 *
	 * @param host    请求主机地址，如：https://api.example.com
	 * @param path    请求路径，如：/files/123
	 * @param method  HTTP方法，固定为"PUT"
	 * @param headers 请求头映射，可以为null
	 * @param querys  查询参数映射，可以为null
	 * @param body    请求体字节数组，可以为null
	 * @return HTTP响应对象
	 * @throws Exception 当请求过程中发生错误时抛出
	 */
	public static HttpResponse doPut(String host, String path, String method,
			Map<String, String> headers,
			Map<String, String> querys,
			byte[] body)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
        	request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

	/**
	 * 执行HTTP DELETE请求。
	 * <p>
	 * 发送DELETE请求，用于删除指定资源。支持查询参数但不支持请求体。
	 *
	 * @param host    请求主机地址，如：https://api.example.com
	 * @param path    请求路径，如：/users/123
	 * @param method  HTTP方法，固定为"DELETE"
	 * @param headers 请求头映射，可以为null
	 * @param querys  查询参数映射，可以为null
	 * @return HTTP响应对象
	 * @throws Exception 当请求过程中发生错误时抛出
	 */
	public static HttpResponse doDelete(String host, String path, String method,
			Map<String, String> headers,
			Map<String, String> querys)
            throws Exception {    	
    	HttpClient httpClient = wrapClient(host);

    	HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

	/**
	 * 构建完整的请求URL。
	 * <p>
	 * 将主机地址、路径和查询参数组合成完整的URL。
	 * 自动处理URL编码和参数拼接。
	 *
	 * @param host   请求主机地址
	 * @param path   请求路径，可以为空
	 * @param querys 查询参数映射，可以为null
	 * @return 构建完成的完整URL
	 * @throws UnsupportedEncodingException 当URL编码失败时抛出
	 */
	private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
    	StringBuilder sbUrl = new StringBuilder();
    	sbUrl.append(host);
    	if (!StringUtils.isBlank(path)) {
    		sbUrl.append(path);
        }
    	if (null != querys) {
    		StringBuilder sbQuery = new StringBuilder();
        	for (Map.Entry<String, String> query : querys.entrySet()) {
        		if (0 < sbQuery.length()) {
        			sbQuery.append("&");
        		}
        		if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
        			sbQuery.append(query.getValue());
                }
        		if (!StringUtils.isBlank(query.getKey())) {
        			sbQuery.append(query.getKey());
        			if (!StringUtils.isBlank(query.getValue())) {
        				sbQuery.append("=");
        				sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
        			}
                }
        	}
        	if (0 < sbQuery.length()) {
        		sbUrl.append("?").append(sbQuery);
        	}
        }

    	return sbUrl.toString();
    }

	/**
	 * 包装HTTP客户端，支持HTTPS连接。
	 * <p>
	 * 创建默认的HTTP客户端，如果请求的是HTTPS地址，则自动配置SSL支持。
	 * 注意：此方法使用信任所有证书的配置，仅适用于开发环境。
	 *
	 * @param host 请求主机地址
	 * @return 配置好的HTTP客户端实例
	 */
	private static HttpClient wrapClient(String host) {
		HttpClient httpClient = new DefaultHttpClient();
		if (host.startsWith("https://")) {
			sslClient(httpClient);
		}

		return httpClient;
	}

	/**
	 * 配置HTTPS客户端的SSL支持。
	 * <p>
	 * 配置SSL上下文以信任所有证书，绕过SSL证书验证。
	 * 警告：此配置不适用于生产环境，存在安全风险。
	 *
	 * @param httpClient HTTP客户端实例
	 */
	private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                	
                }
                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                	
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
        	throw new RuntimeException(ex);
        }
    }
}
