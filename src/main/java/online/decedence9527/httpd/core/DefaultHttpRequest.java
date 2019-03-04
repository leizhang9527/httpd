package online.decedence9527.httpd.core;

import online.decedence9527.httpd.common.HttpMethod;
import online.decedence9527.httpd.common.HttpRequest;

import java.util.List;
import java.util.Map;

/**
 * 内部封装的请求类
 * Author: Decadence
 * Created: 2018/12/5
 */
class DefaultHttpRequest implements HttpRequest {
    
    private final Map<String, String> line;
    
    private final Map<String, String> headers;
    
    private final Map<String, List<String>> params;
    
    DefaultHttpRequest(
            Map<String, String> line,
            Map<String, String> headers,
            Map<String, List<String>> params
    ) {
        this.line = line;
        this.headers = headers;
        this.params = params;
    }
    
    @Override
    public Object getContextValue(String key) {
        return HttpServer.context.get(key);
    }
    
    @Override
    public HttpMethod method() {
        return HttpMethod.lookup(line.get("method"));
    }
    
    @Override
    public String uri() {
        return line.get("uri");
    }
    
    @Override
    public String version() {
        String version = line.get("version");
        return version == null ? "HTTP/1.1" : version;
    }
    
    @Override
    public Map<String, String> header() {
        return headers;
    }
    
    @Override
    public Map<String, List<String>> params() {
        return params;
    }
}
