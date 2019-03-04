package online.decedence9527.httpd.handler;

import online.decedence9527.httpd.common.HttpRequest;
import online.decedence9527.httpd.common.HttpResponse;
import online.decedence9527.httpd.core.HandlerAdapter;

public class IndexHandler extends HandlerAdapter {
    /*
     * Author: Decadence
     * Created: 2018/12/5
     */
    
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.setContentType("text/html; charset=UTF-8");
        response.write("<h1>Hello,I an Java 版本的 HTTP Web 服务器");



//      response.write("<h2>" +
//                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//                + "</h2>");
        response.flush();
    }
}