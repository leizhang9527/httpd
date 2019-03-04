package online.decedence9527.httpd.common;

/**
 * HTTP请求处理类
 * <p>
 * Author: Decadence
 * Created: 2018/12/5
 */
public interface Handler {
    
    /**
     * 处理GET请求
     *
     * @param request  请求
     * @param response 响应
     */
    void doGet(HttpRequest request, HttpResponse response);
    
    /**
     * 处理POST请求
     *
     * @param request 请求
     * @param response 响应
     */
    void doPost(HttpRequest request, HttpResponse response);
}
