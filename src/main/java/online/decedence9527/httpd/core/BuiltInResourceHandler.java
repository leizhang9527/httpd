package online.decedence9527.httpd.core;

import online.decedence9527.httpd.common.HttpRequest;
import online.decedence9527.httpd.common.HttpResponse;
import online.decedence9527.httpd.common.HttpStatus;
import online.decedence9527.httpd.common.SupportedMimeType;
import online.decedence9527.httpd.config.ServerConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 内置关于静态资源的处理器
 * Author: Decadence
 * Created: 2018/12/5
 */
public class BuiltInResourceHandler extends HandlerAdapter {
    
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        ServerConfig serverConfig = (ServerConfig) request.getContextValue(HttpServer.SERVER_CONFIG_KEY);
        
        FileResource fileResource = new FileResource(request.uri(), serverConfig.getWww());
        if (fileResource.exist()) {
            response.setContentType(fileResource.mimeType().getMimeType());
            try (InputStream is = fileResource.inputStream()) {
                byte[] buff = new byte[1024];
                int len;
                while ((len = (is.read(buff))) != -1) {
                    response.write(Arrays.copyOf(buff, len));
                }
            } catch (IOException ignored) {
            
            }
        } else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        response.flush();
    }
    
    static class FileResource {
        
        public final String www;
        
        private final String uri;
        
        private File file;
        
        FileResource(String uri, String www) {
            this.uri = uri;
            this.www = www;
        }
        
        public SupportedMimeType mimeType() {
            int index = uri.lastIndexOf(".");
            return SupportedMimeType.lookup(index == -1 ? null : uri.substring(index + 1));
        }
        
        private void resource() {
            if (file == null) {
                file = new File(www, uri.replace("/", File.separator));
            }
        }
        
        public InputStream inputStream() throws FileNotFoundException {
            this.resource();
            return new FileInputStream(file);
        }
        
        public boolean exist() {
            this.resource();
            return this.file.exists();
        }
    }
}
