## HTTP静态web服务器

### 背景

http 协议被广泛使用，从移动端， PC端浏览器， http 协议无疑是打开互联网应用窗口的重要协议， http在网络应用层中的地位不可撼动，是能准确区分前后台的重要协议。

### 目标

采用B/S模式，编写支持中小型应用的HTTP Web服务器，理解HTTP协议行为。

### 实现

#### 技术

+ 网络编程（Java基础语法，Socket API）
+ 多线程技术
+ HTTP协议理解

#### 环境

+ JDK 1.8
+ IDEA开发工具
+ Maven管理工具

#### 功能

##### 支持方法

+ GET

##### 支持类型

+ html
+ htm
+ css
+ js
+ txt
+ jpeg
+ jpg
+ gif
+ png
+ mp3
+ mp4

#### 扩展

+ 页面缓存

### 应用

+ 编译打包源码

```shell
mvn clean package
```

+ 启动程序（设置参数：端口，静态文件根目录）

```shell
java -jar httpd-1.0.0.jar --port=8888 --www=E:\worskpace\idea-work\java-httpd\static
```

+ 访问静态文件

```reStructuredText
//确保静态文件目录中有index.html文件
http://127.0.0.1:8080/index.html
```