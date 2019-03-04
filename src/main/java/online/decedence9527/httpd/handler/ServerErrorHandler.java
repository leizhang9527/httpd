package online.decedence9527.httpd.handler;

import online.decedence9527.httpd.common.HttpRequest;
import online.decedence9527.httpd.common.HttpResponse;
import online.decedence9527.httpd.common.HttpStatus;
import online.decedence9527.httpd.core.HandlerAdapter;

/**
 * Author: Decadence
 * Created: 2018/12/5
 */
public class ServerErrorHandler extends HandlerAdapter {
   
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.setHttpStatus(HttpStatus.INTERNAL_ERROR);
        response.setContentType("text/html; charset=UTF-8");
        response.write("Server Error");
        response.flush();
    }
}
