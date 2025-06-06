# WiseTransaction
a demo of transaction system

## install and setup Env
### install Java 21 && Maven
you can use sdkman to setup java runtime env 
1. download shell 

```
curl -s "https://get.sdkman.io" | bash
```
2. Initial Env

```
source "$HOME/.sdkman/bin/sdkman-init.sh
```

3. Check Env

```
sdk version
```

4  install java21 and maven-3.9.9

```
sdk install java 21.0.6-oracle

sdk install maven
```
## Package and Run
### 1. package jar
after git clone,you can package jar use 

```
mvn clean package -DSkipTests`
```
Tips: you can config aliyun maven repo,maven is install in ~HOME/.sdkman/candidates/maven/current
you can edit setting.xml in conf dir. add aliyun mirror config in <mirrors>
``` 
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```
### 2. run jar
```
java -jar target/transdemo-0.0.1-SNAPSHOT.jar
```

## AB Test
### 1. install apache2 
```
apt install apache2 -y
```
### 2. run test cmd  1000 request with 50 concurrence
```
ab -n 1000 -c 50 http://127.0.0.1:8088/api/transactions?page=1
```
Tips:**2C2G Ubuntu20.4 test result is below for reference**

```
Server Software:        
Server Hostname:        127.0.0.1
Server Port:            8088

Document Path:          /api/transactions?page=1
Document Length:        885 bytes

Concurrency Level:      50
Time taken for tests:   1.839 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      990000 bytes
HTML transferred:       885000 bytes
Requests per second:    543.87 [#/sec] (mean)
Time per request:       91.934 [ms] (mean)
Time per request:       1.839 [ms] (mean, across all concurrent requests)
Transfer rate:          525.81 [Kbytes/sec] received

Connection Times (ms)
min  mean[+/-sd] median   max
Connect:        0    5  10.0      0      48
Processing:     2   83  68.7     68     366
Waiting:        0   80  69.2     66     366
Total:          2   87  67.6     74     368

Percentage of the requests served within a certain time (ms)
50%     74
66%     95
75%    111
80%    129
90%    177
95%    229
98%    275
99%    340
100%    368 (longest request)
```

## System Design
1.  内存使用ConcurrentHashMap 保存数据,线程安全，且查找效率高。使用stream语法
```
TransactionRepository.java

    private Map<String, Transaction> transactions = new ConcurrentHashMap<String,Transaction>();
    private final CachePersister persister;
```
CachePersister.java 对数据逆行持久化，防止内存数据丢失，定时写文件，线程安全实现

2.  RestFul 风格接口设计，请求参数校验，创建接口保证幂等性
```
TransactionController.java
TransactionService.java
```
3. 重要接口签名与验签。确保接口安全设计,AOP
```
   TransactionController.java
   VerifySignature.java
   SignatureAspect.java
   ParamSign.java
```
4.  统一的数据结构返回和日志打印与全局异常处理，统一的错误码
```
LoggingAspect.java  // AOP 记录请求参数与返回，已经接口访问耗时
GlobalExceptionHandler.java // 全局异常处理
CommonResponse.java  // 通用返回结构
PageResponse.java // 分页返回结构
ResponseFactory.java 
TransactionErrorCode.java //错误码定义
```

5. 交易生命周期定义,黑名单服务,交易规则校验扩展
```
   TransactionLifeCycle.java // 定义交易生命周期
   BlacklistService.java // 简单黑名单服务
   TranscationRules.java // 交易规则校验,可根据实际业务进行扩展开发
   TransferRules.java
   WithDrawRules.java
   DepositRules.java
```