# 简易文件下载Web服务

这是一个使用Java内置HTTP服务器实现的简易文件下载服务，**访问任意路径都会下载指定的本地文件**。

## 功能特性

- 🚀 使用Java内置HTTP服务器，无需额外依赖
- 📁 自动创建测试下载文件
- 🌐 **访问任意路径都响应文件下载**
- 📥 支持文件下载功能
- 🔧 易于配置和扩展
- 📝 请求日志记录

## 快速开始

### 方法1：使用启动脚本

**Windows:**
```bash
# 在项目根目录执行
run-file-server.bat
```

**Linux/Mac:**
```bash
# 在项目根目录执行
chmod +x run-file-server.sh
./run-file-server.sh
```

### 方法2：手动编译运行

```bash
# 1. 编译
javac -cp "target/classes" -d "target/classes" src/main/java/com/cc/safe/fastjson/SimpleFileServer.java

# 2. 运行
java -cp "target/classes" com.cc.safe.fastjson.SimpleFileServer
```

### 方法3：使用Maven

```bash
# 编译项目
mvn compile

# 运行服务器
mvn exec:java -Dexec.mainClass="com.cc.safe.fastjson.SimpleFileServer"
```

## 使用方法

1. 启动服务器后，会在控制台看到如下信息：
   ```
   简易文件服务器已启动！
   访问地址: http://localhost:8080
   访问任意路径都会下载文件: test-download.txt
   例如: http://localhost:8080/任意路径
   按 Ctrl+C 停止服务器
   ```

2. 打开浏览器访问任意路径都会下载文件：
   - http://localhost:8080
   - http://localhost:8080/download
   - http://localhost:8080/test
   - http://localhost:8080/任意路径
   - http://localhost:8080/abc/def/ghi

3. 服务器会自动创建一个测试文件 `test-download.txt`，访问任意URL都会下载这个文件。

## 自定义配置

### 修改端口

在 `SimpleFileServer.java` 中修改 `PORT` 常量：
```java
private static final int PORT = 8080; // 改为你想要的端口
```

### 修改下载文件

在 `SimpleFileServer.java` 中修改 `FILE_PATH` 常量：
```java
private static final String FILE_PATH = "your-file.txt"; // 改为你的文件路径
```

### 添加多文件支持

你可以扩展 `FileDownloadHandler` 来支持多个文件下载，例如：
- `/download/file1` - 下载文件1
- `/download/file2` - 下载文件2

## 注意事项

- 服务器默认运行在 8080 端口
- 确保要下载的文件存在且有读取权限
- 按 Ctrl+C 可以停止服务器
- 这是一个简易服务器，仅用于开发和测试环境

## 扩展功能

你可以基于这个基础服务器添加更多功能：
- 文件列表浏览
- 文件上传功能
- 用户认证
- 日志记录
- 文件类型检测
- 断点续传

## 故障排除

**端口被占用**
- 修改 `PORT` 常量为其他端口号
- 或者停止占用8080端口的其他程序

**文件不存在**
- 检查 `FILE_PATH` 指定的文件是否存在
- 确保文件路径正确且有读取权限

**编译错误**
- 确保Java版本支持（建议Java 8+）
- 检查classpath设置是否正确
