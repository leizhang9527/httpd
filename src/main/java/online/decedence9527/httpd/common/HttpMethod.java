package online.decedence9527.httpd.common;

/**
 * HTTP Web服务器支持的请求方式
 * <p>
 * Author: Decadence
 * Created: 2018/12/5
 */
public enum HttpMethod {
    
    GET,
    
    POST;
    
    public static HttpMethod lookup(String method) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) {
                return httpMethod;
            }
        }
        return null;
    }
}