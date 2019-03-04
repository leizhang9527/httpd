package online.decedence9527.httpd.common;

/**
 * Author: Decadence
 * Created: 2018/12/5
 */
public interface HttpResponse {
    
    /**
     * 根据指定key获取应用数据
     *
     * @param key 指定key
     * @return 全局数据对象
     */
    Object getContextValue(String key);
    
    /**
     * 设置状态
     *
     * @param httpStatus 状态
     */
    void setHttpStatus(HttpStatus httpStatus);
    
    /**
     * 设置响应头
     *
     * @param key   头的key
     * @param value 头的value
     */
    void setHeader(String key, String value);
    
    /**
     * 设置内容类型
     *
     * @param value 类型
     */
    void setContentType(String value);
    
    /**
     * 写数据
     *
     * @param value 数据
     */
    void write(byte[] value);
    
    /**
     * 写数据到缓冲区
     *
     * @param value 数据
     */
    void write(String value);
    
    /**
     * 输出缓冲区内容
     */
    void flush();
}
