package online.decedence9527.httpd.core;

import online.decedence9527.httpd.config.ServerConfig;
import online.decedence9527.httpd.handler.NotFoundHandler;
import online.decedence9527.httpd.handler.NotSupportedMethodHandler;
import online.decedence9527.httpd.handler.ServerErrorHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Decadence
 * Created: 2018/12/5
 */
public abstract class HttpServer {
    
    public static final String SERVER_CONFIG_KEY = "serverConfig";
    
    static final Map<String, Object> context = new HashMap<>();
    
    /**
     * 服务配置
     */
    private final ServerConfig serverConfig;
    
    /**
     * 服务状态
     */
    private final AtomicBoolean serverStatus = new AtomicBoolean(true);
    
    /**
     * 线程调度池
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2,
            new ThreadFactory() {
                
                private final AtomicInteger index = new AtomicInteger(0);
                
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Http-Handler-Thread-" + index.getAndIncrement());
                    return thread;
                }
            });
    
    
    public HttpServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
    
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverConfig.getPort());
            
            DispatcherHandler dispatcherHandler = new DispatcherHandler();
            
            this.loadHandler(dispatcherHandler);
            this.loadContext();
            
            while (serverStatus.get()) {
                Socket socket = serverSocket.accept();
                executorService.execute(dispatcherHandler.handler(socket));
            }
        } catch (IOException e) {
            System.out.println("Server started occur error " + e.getMessage());
        }
    }
    
    /**
     * 加载处理器
     *
     * @param dispatcherHandler
     */
    private void loadHandler(final DispatcherHandler dispatcherHandler) {
        this.builtInHandler(dispatcherHandler);
        this.registerHandler(dispatcherHandler);
    }
    
    /**
     * 加载上下文变量
     */
    private void loadContext() {
        context.put(SERVER_CONFIG_KEY, serverConfig);
        this.registerContext(context);
    }
    
    /**
     * 内置处理器
     */
    private void builtInHandler(final DispatcherHandler dispatcherHandler) {
        //默认加载静态资源处理器
        dispatcherHandler.register("_default_", BuiltInResourceHandler.class);
        dispatcherHandler.register("404", NotFoundHandler.class);
        dispatcherHandler.register("405", NotSupportedMethodHandler.class);
        dispatcherHandler.register("500", ServerErrorHandler.class);
        this.registerHandler(dispatcherHandler);
    }
    
    /**
     * 注册处理器
     *
     * @param dispatcherHandler
     */
    public abstract void registerHandler(final DispatcherHandler dispatcherHandler);
    
    /**
     * 注册上下文变量
     *
     * @param context
     */
    public abstract void registerContext(final Map<String, Object> context);
}
