package online.decedence9527.httpd.common;

import java.util.List;
import java.util.Map;

/**
 * Author: Decadence
 * Created: 2018/12/5
 */
public interface HttpRequest {
    
    /**
     * 根据指定key获取应用数据
     *
     * @param key 指定key
     * @return 全局数据对象
     */
    Object getContextValue(String key);
    
    /**
     * 请求方法
     *
     * @return 方法
     */
    HttpMethod method();
    
    /**
     * 请求地址
     * 比如：
     * <pre>
     * http://127.0.0.1:80/  :  /
     * http://127.0.0.1:80/login  : /login
     * http://127.0.0.1:80/index.html : /index.html
     * http://127.0.0.1:80/css/style.css : /css/style.css
     * </pre>
     *
     * @return 地址
     */
    String uri();
    //URL是URI的子集
    /**
     * 协议版本
     * 默认：HTTP/1.1
     *
     * @return 版本
     */
    String version();
    
    /**
     * 请求头信息
     *
     * @return 头信息
     */
    Map<String, String> header();
    
    /**
     * 请求参数信息
     *
     * @return 参数信息
     */
    Map<String, List<String>> params();
    
}