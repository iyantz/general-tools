# General Tools

通用工具集合项目，提供常用的工具类和方法。

## 项目介绍

本项目是一个基于 Spring Boot 2.5.14 开发的通用工具集合，提供了一系列常用的工具类和方法，帮助开发者提高开发效率。

## 技术栈

- Spring Boot 2.5.14
- Java 8
- Maven
- 主要依赖：
  - Spring Boot Web
  - dom4j (XML处理)
  - Apache HttpClient
  - Apache Commons Lang3
  - FastJSON

## 功能特性

- XML 文件处理工具
- HTTP 请求工具
- 通用工具类集合
- JSON 处理工具

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+

### 构建运行

```bash
# 克隆项目
git clone https://github.com/iyantz/general-tools.git

# 进入项目目录
cd general-tools

# 编译打包
mvn clean package

# 运行项目
java -jar target/general-tools-1.0-SNAPSHOT.jar
```

## 项目结构

```
general-tools
├── src
│   ├── main
│   │   ├── java
│   │   │   └── cn
│   │   │       └── yantz
│   │   │           ├── Application.java
│   │   │           └── xmltools
│   │   └── resources
│   │       └── application.yml
│   └── test
└── pom.xml
```

## 常用工具类合集

### XML 处理工具类

- `XmlTools`: 基于 dom4j 的 XML 文件处理工具类
  - XML 文件读取和解析
  - XML 节点操作
  - XML 文件生成和保存

### HTTP 工具类

- `HttpClientTools`: 基于 Apache HttpClient 的 HTTP 请求工具类
  - GET 请求
  - POST 请求
  - 文件上传
  - 自定义请求头
  - HTTPS 支持

### 通用工具类

- `StringTools`: 基于 Apache Commons Lang3 的字符串处理工具类
  - 字符串判空
  - 字符串分割
  - 字符串格式化
  - 字符串转换

### JSON 工具类

- `JsonTools`: 基于 FastJSON 的 JSON 处理工具类
  - JSON 字符串转对象
  - 对象转 JSON 字符串
  - JSON 文件读写
  - JSON 节点操作

### Bean拷贝工具类

- `BeanCopyUtils`: 对象属性拷贝工具类
  - 基于 Apache BeanUtils 的属性拷贝
  - 支持对象和列表的拷贝
  - 提供忽略 null 值的拷贝方法
  - 支持基本类型转换

### 使用示例

```java
// XML 处理示例
XmlTools xmlTools = new XmlTools();
Document doc = xmlTools.parseXml("config.xml");

// HTTP 请求示例
@Autowired
private HttpClientTemplate httpClientTemplate;

public void example() {
  try {
    String response = httpClientTemplate.doGet("http://example.com");
    System.out.println(response);
  } catch (IOException e) {
    e.printStackTrace();
  }
}

// 字符串处理示例
String text = StringTools.trim("  Hello World  ");

// JSON 处理示例
JsonTools jsonTools = new JsonTools();
User user = jsonTools.parseObject("{\"name\":\"张三\",\"age\":18}", User.class);

// 对象拷贝
UserDTO userDTO = new UserDTO();
BeanCopyUtils.

copyProperties(userEntity, userDTO);

// 创建新对象并拷贝
UserVO userVO = BeanCopyUtils.copy(userDTO, UserVO.class);

// 忽略null值拷贝
BeanCopyUtils.

copyPropertiesIgnoreNull(source, target);

// 列表拷贝
List<UserVO> voList = BeanCopyUtils.copyList(entityList, UserVO.class);
```

## 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进项目。

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 开源协议。

##常用工具类合集
####当前已项目含有工具类
+ XmlReaderUtils
