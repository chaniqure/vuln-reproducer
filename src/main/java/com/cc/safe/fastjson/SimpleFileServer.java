package com.cc.safe.fastjson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 简易的文件下载Web服务器
 * 用于响应本地文件下载请求
 */
public class SimpleFileServer {
    
    private static final int PORT = 80;
    private static final String FILE_PATH = "E:\\code\\safe\\vuln-reproducer\\target\\classes\\com\\cc\\safe\\fastjson\\TouchFile.class"; // 默认下载的文件
    
    public static void main(String[] args) throws IOException {
        // 创建测试文件（如果不存在）
        createTestFileIfNotExists();

        // 创建HTTP服务器
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // 设置文件下载处理器 - 任意路径都响应文件下载
        server.createContext("/", new FileDownloadHandler());

        // 启动服务器
        server.setExecutor(null);
        server.start();

        System.out.println("简易文件服务器已启动！");
        System.out.println("访问地址: http://localhost:" + PORT);
        System.out.println("访问任意路径都会下载文件: " + FILE_PATH);
        System.out.println("例如: http://localhost:" + PORT + "/任意路径");
        System.out.println("按 Ctrl+C 停止服务器");
    }
    
    /**
     * 创建测试文件
     */
    private static void createTestFileIfNotExists() {
        Path filePath = Paths.get(FILE_PATH);
        if (!Files.exists(filePath)) {
            try {
                String content = "这是一个测试下载文件！\n" +
                               "文件创建时间: " + new java.util.Date() + "\n" +
                               "这个文件可以通过浏览器下载。\n" +
                               "访问 http://localhost:8080/download 即可下载此文件。";
                Files.write(filePath, content.getBytes("UTF-8"));
                System.out.println("已创建测试文件: " + FILE_PATH);
            } catch (IOException e) {
                System.err.println("创建测试文件失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 文件下载处理器 - 任意路径都响应文件下载
     */
    static class FileDownloadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String requestPath = exchange.getRequestURI().getPath();

            System.out.println("收到请求: " + method + " " + requestPath);

            if ("GET".equals(method)) {
                handleFileDownload(exchange);
            } else {
                // 不支持的方法
                String response = "Method not allowed. Only GET requests are supported.";
                exchange.sendResponseHeaders(405, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
        
        private void handleFileDownload(HttpExchange exchange) throws IOException {
            Path filePath = Paths.get(FILE_PATH);
            
            if (!Files.exists(filePath)) {
                // 文件不存在
                String response = "File not found: " + FILE_PATH;
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                return;
            }
            
            try {
                // 设置响应头
                exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
                exchange.getResponseHeaders().set("Content-Disposition", 
                    "attachment; filename=\"" + filePath.getFileName().toString() + "\"");
                
                // 获取文件大小
                long fileSize = Files.size(filePath);
                exchange.sendResponseHeaders(200, fileSize);
                
                // 发送文件内容
                try (OutputStream os = exchange.getResponseBody();
                     InputStream is = Files.newInputStream(filePath)) {
                    
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
                
                System.out.println("文件下载完成: " + FILE_PATH + " (请求路径: " + exchange.getRequestURI().getPath() + ")");
                
            } catch (IOException e) {
                System.err.println("文件下载失败: " + e.getMessage());
                String response = "Internal server error";
                exchange.sendResponseHeaders(500, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }
}
