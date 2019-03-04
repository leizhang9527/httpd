package online.decedence9527.httpd.core;

import online.decedence9527.httpd.common.Handler;
import online.decedence9527.httpd.common.HttpRequest;
import online.decedence9527.httpd.common.HttpResponse;
import online.decedence9527.httpd.common.HttpStatus;

/**
 * 处理器适配器
 * Author: Decadence
 * Created: 2018/12/5
 */
public class HandlerAdapter implements Handler {
    
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
        response.flush();
    }
    
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        response.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
        response.flush();
    }
}
