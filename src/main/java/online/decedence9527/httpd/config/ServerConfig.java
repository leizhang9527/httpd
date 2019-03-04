package online.decedence9527.httpd.config;

import java.io.File;

/**
 * Author: Decadence
 * Created: 2018/12/5
 */
public class ServerConfig {
    
    /**
     * 地址
     */
    private String host;
    
    /**
     * 端口
     */
    private Integer port;
    
    /**
     * Web文件目录
     */
    private String www;
    
    private ServerConfig() {
    }
    
    public Integer getPort() {
        return port;
    }
    
    public String getWww() {
        return www;
    }
    
    public void setWww(String www) {
        this.www = www;
    }
    
    public String getHost() {
        return host;
    }
    
    //设计模式：构建者模式
    public static final class ServerConfigBuilder {
        private String host = "127.0.0.1";
        private Integer port = 80;
        private String www = System.getProperty("user.dir") + File.separator + "static";
        
        private ServerConfigBuilder() {
        }
        
        public static ServerConfigBuilder create() {
            return new ServerConfigBuilder();
        }
        
        public ServerConfigBuilder withHost(String host) {
            this.host = host;
            return this;
        }
        
        public ServerConfigBuilder withPort(Integer port) {
            this.port = port;
            return this;
        }
        
        public ServerConfigBuilder withWww(String www) {
            this.www = www;
            return this;
        }
        
        public ServerConfig build() {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.www = this.www;
            serverConfig.port = this.port;
            serverConfig.host = this.host;
            return serverConfig;
        }
    }
}