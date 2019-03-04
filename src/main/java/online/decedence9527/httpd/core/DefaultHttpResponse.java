package online.decedence9527.httpd.core;

import online.decedence9527.httpd.common.HttpRequest;
import online.decedence9527.httpd.common.HttpResponse;
import online.decedence9527.httpd.common.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 内部封装的响应类
 * Author: Decadence
 * Created: 2018/12/5
 */
class DefaultHttpResponse implements HttpResponse {
    
    private HttpStatus httpStatus = HttpStatus.OK;
    
    private Map<String, String> header = new HashMap<>();
    
    private ByteArrayOutputStream content = new ByteArrayOutputStream();
    
    private final HttpRequest request;
    
    private final Socket socket;
    
    DefaultHttpResponse(HttpRequest request, Socket socket) {
        this.request = request;
        this.socket = socket;
    }
    
    @Override
    public Object getContextValue(String key) {
        return HttpServer.context.get(key);
    }
    
    @Override
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
    
    @Override
    public void setHeader(String key, String value) {
        header.put(key, value);
    }
    
    @Override
    public void setContentType(String value) {
        header.put("Content-Type", value);
    }
    
    @Override
    public void write(byte[] value) {
        try {
            content.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void write(String value) {
        try {
            content.write(value.getBytes(StandardCharsets.UTF_8.name()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void flush() {
        try {
            OutputStream out = socket.getOutputStream();
            out.write(
                    DispatcherHandler.encodeContent(
                            request == null ? "HTTP/1.1" : request.version()
                                    + " "
                                    + this.httpStatus.getRequestStatus()
                                    + " "
                                    + this.httpStatus.getDescription()
                                    + "\r\n"));
            
            for (Map.Entry<String, String> header : header.entrySet()) {
                out.write(DispatcherHandler.encodeContent(
                        header.getKey()
                                + ": " +
                                header.getValue()
                                + "\r\n"
                ));
            }
            out.write(DispatcherHandler.encodeContent("\r\n"));
            InputStream inputStream = new ByteArrayInputStream(content.toByteArray());
            byte[] buff = new byte[1024];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
