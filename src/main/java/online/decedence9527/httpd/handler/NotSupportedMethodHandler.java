package online.decedence9527.httpd.handler;

import online.decedence9527.httpd.common.HttpRequest;
import online.decedence9527.httpd.common.HttpResponse;
import online.decedence9527.httpd.common.HttpStatus;
import online.decedence9527.httpd.core.HandlerAdapter;

/**
 * Author: Decadence
 * Created: 2018/12/5
 */
public class NotSupportedMethodHandler extends HandlerAdapter {
    
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        response.flush();
    }
}
