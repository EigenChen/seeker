# 工程依赖和结构规则

## 1. AI角色

你是一名经验丰富的高级Java开发人员，你始终坚持SOLID原则，DRY原则，KISS原则和YAGNI原则。你始终遵循OWASP最佳实践。你总是把任务分解成最小的单元，一步一步地解决任何任务。

## 2. 使用git管理代码

## 3. 分支命名规范

- 主分支：master
- 开发分支：develop
- 发布分支：release
- 功能分支：feature/功能名称
- 修复分支：hotfix/问题描述

## 4. 代码提交信息规范

- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式化
- refactor: 重构
- test: 测试相关
- chore: 构建过程或辅助工具的变动

## 5. 代码审查清单

- 合理使用缓存
- 避免大事务
- 循环中避免数据库操作
- 合理使用索引
- 大量数据查询必须分页
- 代码是否符合编码规范
- 是否有安全漏洞
- 是否有性能问题
- 是否有充分的测试覆盖
- 文档是否同步更新
- 配置是否完整

## 6. 接口定义规范

### 6.1 GET请求规范

```java
    @ApiOperation(value = "查询用户信息")
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCode", value = "用户账号", dataType = "String")
    })
    public ResponseResult<UserDto> getUserInfo(String userCode) {
        return userService.getUserInfo(userCode);
    }

    @ApiOperation(value = "分页查询用户列表")
    @RequestMapping(value = "getUserList", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字检索", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "分页查询，页码", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页查询，每页条数", dataType = "int", defaultValue = "10")
    })
    public PageResponseResult<UserDto> getUserList(String keyword, Integer pageNum, Integer pageSize) {
        return userService.getUserList(userCode, pageNum, pageSize);
    }
```

### 6.2 POST请求规范

```java
    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public ResponseResult<String> addUser(@RequestBody UserInfo userInfo) {
        return userService.addUser(userCode);
    }

    @ApiOperation(value = "分页查询用户列表")
    @RequestMapping(value = "getUserList", method = RequestMethod.POST)
    public PageResponseResult<UserDto> getUserList(@RequestBody UserListQo userListQo) {
        return userService.getUserList(userListQo);
    }
```

## 7. 应用开发规范

- 所有的RestController都要加上 @Api(tags = "业务模块描述") 注解，其中 tags 的内容替换成具体的业务模块描述。
- 所有的请求和响应处理必须只在RestController中完成。
- 所有的请求都要加上 @ApiOperation(value = "接口描述") 注解，其中 value 的内容替换成具体的接口描述。
- 所有数据库操作逻辑都必须在 ServiceImpl 类中完成，该类必须使用Mapper提供的方法。
- ServiceImpl 类不能直接查询数据库，除非绝对必要，否则必须使用Mapper方法。
- RestController 和 ServiceImpl 类之间的数据传输必须只使用dto来完成，反之亦然。
- 实体类只能用于将数据从数据库查询中取出。
- 所有的类、包括内部类都必须添加注释，包括类描述、作者和日期。
- 代码块要使用大括号，即使只有一行。
- 每个类文件末尾留一个空行。
- 代码缩进使用4个空格。
- 在代码中合理记录日志（ERROR、WARN、INFO和DEBUG），敏感信息不允许打印到日志中。
- 严格遵守命名规范，类名使用大驼峰，参数使用小驼峰，方法名使用小驼峰，成员变量使用小驼峰，常量使用全大写。
- flyway脚本文件必须使用纯净格式的日期作为版本号，另外加上对应内容描述信息。
- 建表语句中表名必须全大写。

## 8. 数据库规范

- 表名必须使用大写英文字母，多个单词用下划线分隔。
- 主键名统一使用`TID`，设置为自增主键。
- 字段名必须使用大写字母，多个单词用下划线分隔，必须有字段注释。
- 建表语句必须包含`CREATE_TIME`和`UPDATE_TIME`字段。
- 建表语句必须有表注释。
- 数据集统一为utf-8。

## 10. flyway脚本管理规范

- 脚本文件名必须使用`V{version}__{description}.sql`格式，例如`V20250603__数据库初始化.sql`。
- 脚本文件名中的`{version}`必须使用纯净日期格式，例如`20250603`。
- 脚本文件必须存放在`src/main/resources/db/migration`目录下。

## 11. 技术栈
- JDK版本：Java 8
- 框架：Spring Boot 2.2.11 + Spring Cloud Alibaba 2.2.6
- 构建工具：Maven
- 数据库访问：MyBatis-Plus 3.2.0
- 文档工具：Swagger 2.9.2
- 缓存：Redis
- 消息队列：Kafka 2.3.1
- 注册中心：Nacos 2.0.3
- 日志框架：Log4j2
- 工具库：Hutool 5.7.19、FastJson 1.2.75

## 12. 项目结构规范

```
src/main/java/
    ├── org/suntek/
        ├── common/           # 公共组件
        ├── config/           # 配置类
        ├── controller/       # 控制器
        ├── service/          # 服务接口层
        │   ├── impl/         # 服务实现层
        ├── mapper/           # 数据访问层
        ├── model/            # 数据模型
        │   ├── entity/       # 实体类
        │   ├── dto/          # 数据传输对象
        │   ├── vo/           # 视图对象
        ├── util/             # 工具类
```

## 13. 配置文件规范

```
src/main/resources/
    ├── bootstrap.xml          # Nacos配置文件
    ├── application.yml        # SpringBoot配置文件
    ├── application.properties # 系统配置文件
    ├── log4j2.xml             # Log4j2配置文件
    ├── version.txt            # 版本文件
    ├── mapper/                # MyBatis-Plus的Mapper.xml文件
    ├── db/
        ├── migration/         # flyway脚本文件目录
```

## 14. 实体层（*Entity.java）

- 必须给实体类添加注释，包括实体描述、作者和日期。
- 必须使用@TableName("T_TABLE_NAME")注释，其中表名换成具体的数据表名。
- 必须用@Data（来自Lombok）注释实体类，除非在提示符中另有指定。
- 必须用@TableId(value = "TID", type = IdType.AUTO)注释实体ID，其中value的值替换成大写的ID字段名。
- 必须给字段添加注释，对于字典型字段，描述各个字典值的含义。

## 15. DAO层（*Mapper.java）

- 必须定义成接口类型。
- 必须继承com.baomidou.mybatisplus.core.mapper.BaseMapper<T>，泛型类型替换成对应的实体类。
- 必须添加类注释，包括实体描述、作者和日期。

## 16. Service层（*Service.java）

- 服务类必须为接口类型。
- 所有服务类方法的实现都必须在实现该服务类的"ServiceImpl"类中进行。
- 服务类必须继承com.baomidou.mybatisplus.extension.service.IService<T>，泛型替换成对应实体类。

## 17. ServiceImpl层（*ServiceImpl.java）

- 所有的 ServiceImpl 类都必须添加 @Service 注解。
- 在 ServiceImpl 类中，所有的依赖项都必须通过 @Autowired 注解进行注入，且不能通过构造函数注入，除非另有说明。
- ServiceImpl 方法的返回对象应当是数据传输对象（DTO），而非实体类，除非绝对必要。
- 对于任何需要检查记录是否存在的情况，应使用相应的Dao层方法。
- 对于任何连续的多次数据库执行操作，必须使用@Transactional注释方法，且依赖的子方法必须是public访问修饰符。

## 18. 数据传输对象层（*Dto.java）

- 必须使用@ApiModel("类描述")注释，其中类描述替换成具体的业务数据描述。
- 必须使用@ApiModelProperty("字段描述")，其中字段描述替换成对应字段的含义。

## 15. 接口层（*Controller.java）

- 必须为控制器类添加 @RestController 注解。
- 必须使用 @RequestMapping 注解来指定类级别的 API 路由，例如 ("/api/user") 。
- 类方法必须使用最佳实践的 HTTP 方法注解，例如：create = @postMapping("/create") 等。
- 在类方法中，所有的依赖项都必须使用 @Autowired 注解进行注入，且不能通过构造函数注入，除非另有说明。
- 方法返回的对象必须为"响应实体"类型，且其具体子类型为"响应信息"类型。
- 在捕获块中发现的错误必须由"自定义全局异常处理程序"类来处理。

## 16. MybatisPlus文件（*Mapper.xml）

- *Mapper.xml必须存放在 resources/mapper 目录下。
- 必须定义一个<resultMap><resultMap/>，描述结果集的映射关系。
- 必须定义一个<sql><sql/>，描述实体表的字段列表。

## 17. 响应实体类规范

响应实体类必须照搬以下内容，放到 vo 包路径下：

### 17.1 ResponseResult.java - 通用返回结果信息类

```java
@Data
@ApiModel("ResponseResult-通用返回结果信息")
public class ResponseResult<T> {
 public static final int STATUS_CODE_SUCCESS = 200;
 public static final int STATUS_CODE_FAIL = 501;

 public static final int OP_CODE_SUCCESS = 0;
 public static final int OP_CODE_FAIL = 1;

 @ApiModelProperty("接口操作返回码，200:成功,其他异常")
 private int statusCode;
 @ApiModelProperty("返回提示消息")
 private String opDesc;
 private int opCode = OP_CODE_SUCCESS;
 private String businessCode = AppConstants.APP_NAME;
 private String localTime = DateTimeUtils.formatDate(new Date(), DateTimeUtils.TSFORMAT);

 @ApiModelProperty("返回的数据")
 private T data;

 /**
  *
  * @Title 判断是否是成功返回
  * @param <T> 泛型结果
  * @param retMap 返回结果结构体
  * @return boolean
  * @author xxx
  * @date 2022年3月15日 下午1:35:25
  */
 public static <T> boolean isSuccess(ResponseResult<T> retMap) {
  return retMap.getOpCode() == OP_CODE_SUCCESS ? true : false;
 }

 /**
  *
  * @Title 说明
  * @param <T> 泛型结果
  * @param msg 成功消息
  * @return ResponseResult<T>
  * @author xxx
  * @date 2022年3月15日 下午1:35:08
  */
 public static <T> ResponseResult<T> success(String msg) {
  ResponseResult<T> r = new ResponseResult<T>();
  r.setStatusCode(STATUS_CODE_SUCCESS);
  r.setOpDesc(msg);
  return r;
 }

 /**
  *
  * @Title 说明
  * @param <T> 泛型结果
  * @param data 返回数据
  * @return ResponseResult<T>
  * @author xxx
  * @date 2022年3月15日 下午1:35:01
  */
 public static <T> ResponseResult<T> success(T data) {
  ResponseResult<T> r = new ResponseResult<T>();
  r.setStatusCode(STATUS_CODE_SUCCESS);
  r.setOpDesc("成功");
  r.setData(data);
  return r;
 }

 /**
  *
  * @Title 说明
  * @param <T> 泛型结果
  * @param data 返回数据
  * @param msg 成功消息
  * @return ResponseResult<T>
  * @author xxx
  * @date 2022年3月15日 下午1:34:52
  */
 public static <T> ResponseResult<T> success(T data, String msg) {
  ResponseResult<T> r = new ResponseResult<T>();
  r.setStatusCode(STATUS_CODE_SUCCESS);
  r.setOpDesc(msg);
  r.setData(data);
  return r;
 }

 /**
  *
  * @Title 说明
  * @param <T> 泛型结果
  * @param msg 错误消息
  * @return ResponseResult<T>
  * @author xxx
  * @date 2022年3月15日 下午1:34:37
  */
 public static <T> ResponseResult<T> fail(String msg) {
  ResponseResult<T> r = new ResponseResult<T>();
  r.setStatusCode(STATUS_CODE_FAIL);
  r.setOpCode(OP_CODE_FAIL);
  r.setOpDesc(msg);
  return r;
 }

 /**
  *
  * @Title 说明
  * @param <T> 泛型实体
  * @param statusCode 业务状态码
  * @param msg 错误消息
  * @return ResponseResult<T>
  * @author xxx
  * @date 2022年3月15日 下午1:34:14
  */
 public static <T> ResponseResult<T> fail(int statusCode, String msg) {
  ResponseResult<T> r = new ResponseResult<T>();
  r.setStatusCode(statusCode);
  r.setOpCode(OP_CODE_FAIL);
  r.setOpDesc(msg);
  return r;
 }
}
```

### 17.2 PageResponseResult.java - 分页接口响应实体类

```java
@Data
public class PageResponseResult<T> {

    @ApiModelProperty("接口状态码，200：正常")
    @JsonProperty(value = "StatusCode")
    private Integer statusCode;

    @ApiModelProperty("模块代码")
    @JsonProperty(value = "BusinessCode")
    private String businessCode;

    @ApiModelProperty("业务操作代码，0:成功,1:失败,>1:异常")
    @JsonProperty(value = "OpCode")
    private int opCode;

    @ApiModelProperty("业务操作描述")
    @JsonProperty(value = "OpDesc")
    private String opDesc;

    @ApiModelProperty("本次搜索中的页码")
    @JsonProperty(value = "PageNum")
    private Integer pageNum;

    @ApiModelProperty("本次搜索每页大小")
    @JsonProperty(value = "PageSize")
    private Integer pageSize;

    @ApiModelProperty("本次搜索中的总页数")
    @JsonProperty(value = "Pages")
    private Integer pages;

    @ApiModelProperty("总记录数大小")
    @JsonProperty(value = "Total")
    private Long total;

    @ApiModelProperty("时间戳")
    @JsonProperty(value = "LocalTime")
    private String localTime;

    @ApiModelProperty("分页查询当前页数据列表")
    @JsonProperty(value = "Data")
    private List<T> data;

    @ApiModelProperty("用于分页查询的标识id")
    private String scrollId;

    /**
     * @param <T> 泛型结果
     * @param msg 成功消息
     * @return ResponseResult<T>
     * @Title 说明
     * @author xxx
     * @date 2022年3月15日 下午1:35:08
     */
    public static <T> PageResponseResult<T> success(String msg) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setStatusCode(ResponseResult.STATUS_CODE_SUCCESS);
        r.setOpCode(ResponseResult.OP_CODE_SUCCESS);
        r.setOpDesc(msg);
        return r;
    }

    /**
     * @param <T>  泛型结果
     * @param data 返回数据
     * @return ResponseResult<T>
     * @Title 说明
     * @author xxx
     * @date 2022年3月15日 下午1:35:01
     */
    public static <T> PageResponseResult<T> success(List<T> data) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setStatusCode(ResponseResult.STATUS_CODE_SUCCESS);
        r.setOpCode(ResponseResult.OP_CODE_SUCCESS);
        r.setOpDesc("成功");
        r.setData(data);
        return r;
    }

    public static <T> PageResponseResult<T> success(IPage<T> data) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setTotal(data.getTotal());
        r.setData(data.getRecords());
        r.setPages((int) data.getPages());
        r.setPageNum((int) data.getCurrent());
        r.setPageSize((int) data.getSize());
        r.setStatusCode(ResponseResult.STATUS_CODE_SUCCESS);
        r.setOpCode(ResponseResult.OP_CODE_SUCCESS);
        return r;
    }

    public static <T> PageResponseResult<T> success(Page<T> data) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setTotal(data.getTotal());
        r.setData(data.getResult());
        r.setPages(data.getPages());
        r.setPageNum(data.getPageNum());
        r.setPageSize(data.getPageSize());
        r.setStatusCode(ResponseResult.STATUS_CODE_SUCCESS);
        r.setOpCode(ResponseResult.OP_CODE_SUCCESS);
        return r;
    }

    /**
     * @param <T> 泛型结果
     * @param msg 错误消息
     * @return ResponseResult<T>
     * @Title 说明
     * @author xxx
     * @date 2022年3月15日 下午1:34:37
     */
    public static <T> PageResponseResult<T> fail(String msg) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setStatusCode(ResponseResult.STATUS_CODE_FAIL);
        r.setOpCode(ResponseResult.OP_CODE_FAIL);
        r.setOpDesc(msg);
        return r;
    }

    /**
     * @param <T>        泛型实体
     * @param statusCode 业务状态码
     * @param msg        错误消息
     * @return ResponseResult<T>
     * @Title 说明
     * @author xxx
     * @date 2022年3月15日 下午1:34:14
     */
    public static <T> PageResponseResult<T> fail(int statusCode, String msg) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setStatusCode(statusCode);
        r.setOpCode(ResponseResult.OP_CODE_FAIL);
        r.setOpDesc(msg);
        return r;
    }
}
```

## 18. 全局异常处理规范

全局异常处理必须照搬规范内容，放到config目录下：

### 18.1 ErrorHandleConfig.java - 全局异常处理配置类

```java
@RestControllerAdvice
@Slf4j
@ResponseBody
public class ErrorHandleConfig {

 /**
  * @param ex 运行时异常
  * @return 错误消息
  */
 @ExceptionHandler(RuntimeException.class)
 public ResponseResult<String> runtimeExceptionHandler(RuntimeException ex) {
  return resultFormat(ErrorCode.RuntimeException.getCode(), ex);
 }

 /**
  *
  * @param ex 空指针异常
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler(NullPointerException.class)
 public ResponseResult<String> nullPointerExceptionHandler(NullPointerException ex) {
  return resultFormat(ErrorCode.NullPointerException.getCode(), ex);
 }

 /**
  *
  * @param ex 类型转换异常
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler(ClassCastException.class)
 public ResponseResult<String> classCastExceptionHandler(ClassCastException ex) {
  return resultFormat(ErrorCode.ClassCastException.getCode(), ex);
 }

 /**
  *
  * @param ex IO异常
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler(IOException.class)
 public ResponseResult<String> iOExceptionHandler(IOException ex) {
  return resultFormat(ErrorCode.IOException.getCode(), ex);
 }

 /**
  *
  * @param ex 未知方法异常
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler(NoSuchMethodException.class)
 public ResponseResult<String> noSuchMethodExceptionHandler(NoSuchMethodException ex) {
  return resultFormat(ErrorCode.NoSuchMethodException.getCode(), ex);
 }

 /**
  *
  * @param ex 数组越界异常
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler(IndexOutOfBoundsException.class)
 public ResponseResult<String> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
  return resultFormat(ErrorCode.IndexOutOfBoundsException.getCode(), ex);
 }

 /**
  *
  * @param ex 400错误
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler({ HttpMessageNotReadableException.class })
 public ResponseResult<String> requestNotReadable(HttpMessageNotReadableException ex) {
  return resultFormat(ErrorCode.HttpMediaTypeNotAcceptableException.getCode(), ex);
 }

 /**
  *
  * @param ex 400错误
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler({ TypeMismatchException.class })
 public ResponseResult<String> requestTypeMismatch(TypeMismatchException ex) {
  return resultFormat(ErrorCode.TypeMismatchException.getCode(), ex);
 }

 /**
  *
  * @param ex 400错误
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler({ MissingServletRequestParameterException.class })
 public ResponseResult<String> requestMissingServletRequest(MissingServletRequestParameterException ex) {
  return resultFormat(ErrorCode.MissingServletRequestParameterException.getCode(), ex);
 }

 /**
  *
  * @param ex 405错误
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
 public ResponseResult<String> request405(HttpRequestMethodNotSupportedException ex) {
  return resultFormat(ErrorCode.HttpRequestMethodNotSupportedException.getCode(), ex);
 }

 /**
  *
  * @param ex 406错误
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
 public ResponseResult<String> request406(HttpMediaTypeNotAcceptableException ex) {
  return resultFormat(ErrorCode.HttpMediaTypeNotAcceptableException.getCode(), ex);
 }

 /**
  *
  * @Title 说明
  * @param ex 500错误
  * @return ResponseResult<String> 错误结果
  * @author xxx
  * @date 2022年3月15日 上午9:55:22
  */
 @ExceptionHandler({ ConversionNotSupportedException.class, HttpMessageNotWritableException.class })
 public ResponseResult<String> server500(RuntimeException ex) {
  return resultFormat(ErrorCode.ConversionNotSupportedException.getCode(), ex);
 }

 /**
  *
  * @param ex 栈溢出
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler({ StackOverflowError.class })
 public ResponseResult<String> requestStackOverflow(StackOverflowError ex) {
  return resultFormat(ErrorCode.StackOverflowError.getCode(), ex);
 }

 /**
  *
  * @param ex 除数不能为0
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler({ ArithmeticException.class })
 public ResponseResult<String> arithmeticException(ArithmeticException ex) {
  return resultFormat(ErrorCode.ArithmeticException.getCode(), ex);
 }

 /**
  *
  * @Title 说明 参数验证错误
  * @param ex 参数验证错误异常
  * @return ResponseResult<String>
  * @author xxx
  * @date 2022年3月15日 上午9:36:52
  */
 @ExceptionHandler({ MethodArgumentNotValidException.class })
 public ResponseResult<String> handleBindException(MethodArgumentNotValidException ex) {
  StringBuilder sb = new StringBuilder("");
  for (FieldError error : ex.getBindingResult().getFieldErrors()) {
   if (sb.length() == 0) {
    sb.append(error.getDefaultMessage());
   } else {
    sb.append(", ").append(error.getDefaultMessage());
   }
  }
  ResponseResult<String> ret = ResponseResult.fail(sb.toString());
  ret.setStatusCode(ErrorCode.MethodArgumentNotValidException.getCode());
  return ret;
 }

 /**
  *
  *
  * @param ex 其他错误
  * @return ResponseResult<String> 错误结果
  */
 @ExceptionHandler({ Exception.class })
 public ResponseResult<String> exception(Exception ex) {
  return resultFormat(ErrorCode.Exception.getCode(), ex);
 }

 /**
  *
  * @Title 说明 异常封装
  * @param <T>
  * @param code 异常编码
  * @param ex   异常类
  * @return ResponseResult<String>
  * @author xxx
  * @date 2022年3月15日 上午9:37:27
  */
 private <T extends Throwable> ResponseResult<String> resultFormat(Integer code, T ex) {
  ex.printStackTrace();
  StackTraceElement[] stackTrace = ex.getStackTrace();
  for (int i = 0; i < stackTrace.length; i++) {
   log.debug(stackTrace[i].getClassName() + "." + stackTrace[i].getMethodName() + ":"
     + stackTrace[i].getLineNumber());
  }
  ResponseResult<String> ret = ResponseResult.fail(ex.getMessage());
  ret.setStatusCode(code);
  return ret;
 }

 /**
  *
  * All rights Reserved, Designed By https://www.pcitech.com
  *
  * @Title: ErrorCode
  * @Description: 异常码定义
  * @author: xxx
  * @date: 2022年3月15日 上午9:46:52
  * @version v1.0
  * @Copyright: 2022 https://www.pcitech.com Inc. All rights reserved.
  * @warning: 本内容仅限于佳都新太科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目
  */
 enum ErrorCode {
  RuntimeException(502), NullPointerException(503), ClassCastException(504), IOException(505),
  NoSuchMethodException(506), IndexOutOfBoundsException(507), HttpMessageNotReadableException(508),
  TypeMismatchException(509), MissingServletRequestParameterException(10),
  HttpRequestMethodNotSupportedException(511), HttpMediaTypeNotAcceptableException(512),
  ConversionNotSupportedException(513), StackOverflowError(514), ArithmeticException(515),
  MethodArgumentNotValidException(516), Exception(17);

  int code;

  ErrorCode(int code) {
   this.code = code;
  }

  int getCode() {
   return code;
  }
 }
}
```

## 19. 全局web拦截器规范

全局web拦截器必须照搬规范内容，放到config目录下：

### 19.1 WebConfig.java - 全局web拦截器配置类

```java
@Configuration
@EnableWebMvc
@EnableSwagger2
public class WebConfig implements ApplicationContextAware, WebMvcConfigurer {

 /**
  * swagger的开关
  */
 @Value("${swagger.enable:true}")
 private Boolean enable;

 @Override
 public void setApplicationContext(ApplicationContext arg0) throws BeansException {
 }

 @Override
 public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
  for (int i = converters.size() - 1; i >= 0; i--) {
   if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
    converters.remove(i);
   }
  }

  FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

  FastJsonConfig fastJsonConfig = new FastJsonConfig();
  fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteDateUseDateFormat,
    SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);

  fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
  // 首字母大写
  SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();

  NameFilter nameFilter = (object, name, value) -> name;
  serializeConfig.addFilter(UiConfiguration.class, nameFilter);
  serializeConfig.addFilter(SwaggerResource.class, nameFilter);

  serializeConfig.setPropertyNamingStrategy(PropertyNamingStrategy.PascalCase);
  fastJsonConfig.setSerializeConfig(serializeConfig);

  fastConverter.setFastJsonConfig(fastJsonConfig);

  List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
  supportedMediaTypes.add(MediaType.APPLICATION_JSON);
  fastConverter.setSupportedMediaTypes(supportedMediaTypes);
  converters.add(fastConverter);
 }

 /**************** swagger-ui相关配置、静态资源映射路径 ****************/
 @Override
 public void addResourceHandlers(ResourceHandlerRegistry registry) {
//  registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
  registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
  registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
 }

 /**
  *
  * @Title 说明
  * @return Docket
  * @author xxx
  * @date 2022年3月15日 下午12:16:12
  */
 @Bean
 public Docket createRestApi() {
  return new Docket(DocumentationType.SWAGGER_2).enable(enable).apiInfo(apiInfo()).select()
    .apis(RequestHandlerSelectors.basePackage("com.suntek.produce.monitor")).paths(PathSelectors.any())
    .build();
 }

 private ApiInfo apiInfo() {
  return new ApiInfoBuilder().title("produce-monitor-serv模板项目模块接口").description("本工程提供模板项目接口服务")
    .termsOfServiceUrl("http://localhost:20253/doc.html").version("v1.0.0").build();
 }

 /**
  * URL不区分大小写
  */
 @Override
 public void configurePathMatch(PathMatchConfigurer configurer) {
  AntPathMatcher pathMatcher = new AntPathMatcher();
  pathMatcher.setCaseSensitive(false);
  configurer.setPathMatcher(pathMatcher);
 }

 /**
  * @return FilterRegistrationBean<SsoClientFilter>
  * @Title 接入统一授权
  * @author xxx
  * @date 2020年12月17日 下午2:14:53
  */
 @Bean
 public FilterRegistrationBean<SsoClientFilter> securityFilterRegistration() {
  FilterRegistrationBean<SsoClientFilter> registration = new FilterRegistrationBean<>();
  registration.setFilter(new SsoClientFilter());
  registration.setName("SsoClientFilter");
  /**
   * 需要接入统一授权url
   */
  registration.addUrlPatterns("/*");
  registration.setOrder(0);
  return registration;
 }
}
```

## 20. 规范说明

本开发规范文档定义了以下关键内容：

### 核心规范
- AI角色定义和开发原则
- 应用开发规范和代码风格要求
- 数据库设计和命名规范
- flyway脚本管理规范

### 技术栈和架构规范
- 技术栈版本要求
- 项目结构规范
- 配置文件规范

### 分层开发规范
- 实体层（Entity）开发规范
- DAO层（Mapper）开发规范
- Service层接口规范
- ServiceImpl层实现规范
- 数据传输对象（DTO）规范
- 接口层（Controller）规范
- MybatisPlus文件规范

### 通用组件规范
- 响应实体类规范
- 全局异常处理规范
- 全局web拦截器规范

**注意：本规范将在后续的 vibe coding 开发过程中严格遵循执行。**
