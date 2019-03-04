package online.decedence9527.httpd;

import online.decedence9527.httpd.config.ServerConfig;
import online.decedence9527.httpd.core.DispatcherHandler;
import online.decedence9527.httpd.core.HttpServer;
import online.decedence9527.httpd.handler.IndexHandler;

import java.io.File;
import java.util.Map;

public class Httpd {
    
    public static void main(String[] args) {
        
        ServerConfig.ServerConfigBuilder builder = ServerConfig.ServerConfigBuilder.create();
        
        //处理程序运行时提供的参数
        //地址，端口号，文件目录
        for (String arg : args) {
            String[] kv = arg.split("=");
            if (kv.length == 2) {
                String paramName = kv[0];
                String paramValue = kv[1];
                if (paramName.equals("--host")) {
                    builder.withHost(paramValue);
                }
                if (paramName.equals("--port")) {
                    try {
                        builder.withPort(Integer.parseInt(paramValue));
                    } catch (NumberFormatException e) {
                        System.out.println("Parameter --port=value value must be a number 0-65535.");
                        System.exit(1);
                    }
                }
                if (paramName.equals("--www")) {
                    File file = new File(paramValue);
                    if (file.exists() && file.isDirectory()) {
                        builder.withWww(paramValue);
                    } else {
                        System.out.println("Parameter --www=value value must be a directory exists on file system.");
                        System.exit(1);
                    }
                }
            }
        }
        
        ServerConfig serverConfig = builder.build();
        HttpServer httpServer = new HttpServer(serverConfig) {
            @Override
            public void registerHandler(DispatcherHandler dispatcherHandler) {
                dispatcherHandler.register("/", IndexHandler.class);
            }
            
            @Override
            public void registerContext(Map<String, Object> context) {
            
            }
        };
        try {
            System.out.println(
                    String.format("Server start on %s:%s  www = %s",
                            serverConfig.getHost(),
                            serverConfig.getPort(),
                            serverConfig.getWww()
                    )
            );
            //启动Web服务
            httpServer.start();
        } catch (Throwable ignored) {
        }
    }
}