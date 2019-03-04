package online.decedence9527.httpd.core;

import online.decedence9527.httpd.common.Handler;
import online.decedence9527.httpd.common.HttpMethod;
import online.decedence9527.httpd.common.HttpRequest;
import online.decedence9527.httpd.common.HttpResponse;
import online.decedence9527.httpd.common.SupportedMimeType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 分发处理类
 * Author: Decadence
 * Created: 2018/12/5
 */
public final class DispatcherHandler {
    
    private Map<String, Handler> registerHandler = new HashMap<>();
    
    public void register(String url, Class<? extends Handler> clazz) {
        try {
            Handler handler = clazz.getConstructor().newInstance();
            registerHandler.put(url, handler);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    Runnable handler(final Socket socket) {
        return () -> {
            HttpReqRespWrapper wrapper = new HttpReqRespWrapper(socket);
            HttpRequest request = wrapper.httpRequest();
            HttpResponse response = wrapper.httpResponse();
            try {
                if (request.uri() == null) {
                    registerHandler.get("404").doGet(request, response);
                    return;
                }
                if (request.method() == null ||
                        HttpMethod.lookup(request.method().name()) == null) {
                    registerHandler.get("405").doGet(request, response);
                    return;
                }
                String uri = request.uri();
                if (isStaticResource(uri)) {
                    Handler handler = registerHandler.get("_default_");
                    handler.doGet(request, response);
                } else {
                    Handler handler = registerHandler.get(uri);
                    if (handler == null) {
                        registerHandler.get("404").doGet(request, response);
                    } else {
                        HttpMethod method = request.method();
                        if (method == HttpMethod.GET) {
                            handler.doGet(request, response);
                        } else if (method == HttpMethod.POST) {
                            handler.doPost(request, response);
                        } else {
                            registerHandler.get("405").doGet(request, response);
                        }
                    }
                }
            } catch (Exception e) {
                registerHandler.get("500").doGet(request, response);
            }
        };
    }
    
    private static boolean isStaticResource(String url) {
        return SupportedMimeType.lookup(url.substring(url.lastIndexOf(".") + 1)) != null;
    }
    
    static byte[] encodeContent(String value) {
        return value.getBytes();
    }
    
    private static String decodePercent(String str) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }
        return decoded;
    }
    
    private static Map<String, List<String>> decodeParameters(String queryString) {
        Map<String, List<String>> params = new HashMap<>();
        if (queryString != null) {
            StringTokenizer st = new StringTokenizer(queryString, "&");
            while (st.hasMoreTokens()) {
                String e = st.nextToken();
                int sep = e.indexOf('=');
                String propertyName = sep >= 0 ? decodePercent(e.substring(0, sep)).trim() : decodePercent(e).trim();
                if (!params.containsKey(propertyName)) {
                    params.put(propertyName, new ArrayList<>());
                }
                String propertyValue = sep >= 0 ? decodePercent(e.substring(sep + 1)) : null;
                if (propertyValue != null) {
                    params.get(propertyName).add(propertyValue);
                }
            }
        }
        return params;
    }
    
    private static class HttpReqRespWrapper {
        
        private final Socket socket;
        
        private HttpRequest request;
        
        private HttpResponse response;
        
        private HttpReqRespWrapper(Socket socket) {
            this.socket = socket;
            this.handleSocket();
        }
        
        private void handleSocket() {
            Map<String, String> line = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            Map<String, List<String>> params = new HashMap<>();
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String lineValue = reader.readLine();
                StringTokenizer tokenizer = null;
                if (lineValue != null) {
                    tokenizer = new StringTokenizer(lineValue);
                }
                if (tokenizer != null && tokenizer.hasMoreTokens()) {
                    line.put("method", tokenizer.nextToken());
                }
                if (tokenizer != null && tokenizer.hasMoreTokens()) {
                    String uri = tokenizer.nextToken();
                    int qmi = uri.indexOf('?');
                    if (qmi >= 0) {
                        params = decodeParameters(uri.substring(qmi + 1));
                        uri = decodePercent(uri.substring(0, qmi));
                    } else {
                        uri = decodePercent(uri);
                    }
                    line.put("uri", uri);
                }
                if (tokenizer != null && tokenizer.hasMoreTokens()) {
                    line.put("version", tokenizer.nextToken());
                } else {
                    line.put("version", "HTTP/1.1");
                }
                lineValue = reader.readLine();
                while (lineValue != null && !lineValue.trim().isEmpty()) {
                    int p = lineValue.indexOf(':');
                    if (p >= 0) {
                        headers.put(
                                lineValue.substring(0, p).trim().toLowerCase(Locale.US),
                                lineValue.substring(p + 1).trim());
                    }
                    lineValue = reader.readLine();
                }
            } catch (IOException ignored) {
            
            }
            this.request = new DefaultHttpRequest(line, headers, params);
            this.response = new DefaultHttpResponse(this.request, socket);
        }
        
        HttpResponse httpResponse() {
            return this.response;
        }
        
        HttpRequest httpRequest() {
            return this.request;
        }
    }
}