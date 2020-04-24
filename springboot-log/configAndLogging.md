## 1、SpringBoot的配置文件

> SpringBoot使用一个全局的配置文件，文件名固定为
>
> application.propertites或者application.yml

+ 配置文件的作用：修改自动配置的默认值或者配置一些环境

YAML（YAML Ain't Markup Language）：yaml是以数据为中心，比json xml更适合做配置文件

```yaml
server:
  port: 8081
```

XML：

```xml
<server>
	<port>8081</port>
</server>
```

### 1.1、yaml基本语法

**字面量：普通的值（数字，字符串，布尔）**

​	k: v 	字符串默认不用加上单引号或者双引号

​		""：双引号；不会转义字符串里面的特殊字符；特殊字符会作为本身想表示的意思

​				name:   "abc \n 123"：输出：zhangsan 换行  123

​		''：单引号；会转义特殊字符，特殊字符最终只是一个普通的字符串数据

​				name:   ‘abc \n 123’：输出：abc \n 123

**对象、Map（属性和值）（键值对）：**

​	k: v	在下一行来写对象的属性和值的关系

​		对象还是k: v的方式

```yaml
friends:
		lastName: jack
		age: 20
# 行内写法
friends: {lastName: jack,age: 18}
```

**数组（List、Set）**

用 - 表示数组中的一个元素

```yaml
animal:
 - cat
 - dog
 - pig
# 行内写法
animal: [cat,dog,pig]
```

### 1.2、配置文件注入值

配置文件

```yaml
person:
    lastName: hello
    age: 18
    boss: false
    birth: 2017/12/12
    maps: {k1: v1,k2: 12}
    lists:
      - lisi
      - zhaoliu
    dog:
      name: 小狗
      age: 12
```

javaBean

```java
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定；
 *      prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 *
 * 只有这个组件是容器中的组件，才能容器提供的@ConfigurationProperties功能；
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String lastName;
    private Integer age;
    private Boolean boss;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
```

导入配置文件处理器，以后编写配置就有提示

```xml
<!--导入配置文件处理器，配置文件进行绑定就会有提示-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

### 1.3、properties配置文件注入值

```java
@Component
@ConfigurationProperties(prefix = "person")
@Validated // 开启数据校验
public class Person {

    @NotNull
    private String email;

    @Value("${person.last-name}")
    private String lastName;
    @Value("#{11*2}")
    private Integer age;
    @Value("true")
    private Boolean boss;

    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
}
```

配置文件

```properties
person.last-name=${random.uuid}-lastName
person.age=${random.int(10,30)}
person.birth=2019/12/15
person.boss=false
person.maps.k1=key1
person.maps.k2=14
person.lists=a,b,c
```

原理：**@PropertySource&@ImportResource&@Bean**

@**PropertySource**：加载指定的配置文件

```java
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定；
 *      prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 * 只有这个组件是容器中的组件，才能容器提供的@ConfigurationProperties功能；
 *  @ConfigurationProperties(prefix = "person")默认从全局配置文件中获取值；
 */
@PropertySource(value = {"classpath:person.properties"})
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
```

@**ImportResource**：导入Spring的配置文件，让配置文件里面的内容生效

Spring Boot里面没有Spring的配置文件，我们自己编写的配置文件，也不能自动识别

想让Spring的配置文件生效，加载进来；@**ImportResource**标注在一个配置类上

```java
@ImportResource(locations = {"classpath:beans.xml"})
```

SpringBoot推荐给容器中添加组件的方式

+ **@Configuration**配置类替代xml配置文件

+ 使用**@Bean**给容器中添加组件

### 1.4、配置文件的占位符

**随机数**

```java
${random.value}、${random.int}、${random.long}
${random.int(10)}、${random.int[1024,65536]}
```

**默认值**

```properties
car.name=${person.age:hello}
```

### 1.5、Profile文件

配置文件命名可以是：application-{profile}.properties/yml，默认使用application.properties

yaml切分文档块

```yaml
server:
  port: 8081
spring:
  profiles:
    active: prod	#激活哪一个环境

---
server:
  port: 8083
spring:
  profiles: dev

---
server:
  port: 8084
spring:
  profiles: prod  #指定属于哪个环境
```

#### **配置文件激活方法**

​	1）在配置文件中指定  spring.profiles.active=dev

​	2）命令行：

​		java -jar loggingconfig-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev；

​		可以直接在测试的时候，配置传入命令行参数

​	3）虚拟机参数；

​		-Dspring.profiles.active=dev

#### **指定配置文件位置**

springboot 启动会扫描以下位置的application.properties或者application.yml文件作为Spring boot的默认配置文件

**–file:./config/**

**–file:./**

**–classpath:/config/**

**–classpath:/**

优先级由高到底，高优先级的配置会覆盖低优先级的配置

SpringBoot会从这四个位置全部加载主配置文件，**互补配置**

+ 还可以通过spring.config.location来改变默认的配置文件位置

**项目打包好以后，我们可以使用命令行参数的形式，启动项目的时候来指定配置文件的新位置；指定配置文件和默认加载的这些配置文件共同起作用形成互补配置**

java -jar loggingconfig-0.0.1-SNAPSHOT.jar --spring.config.location=F:/application.properties

#### 配置文件加载顺序

以下配置优先级由高到低，高优先级覆盖低优先级的配置，所有的配置都会加载到。[详见这里](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#boot-features-external-config)

+ 命令行参数，所有的配置都可以在命名行上指定，多个配置用空格分隔

```shell
java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties
```

+ JNDI属性来自`java:comp/env`。
+ Java系统属性（`System.getProperties()`）。
+ OS环境变量。
+ **RandomValuePropertySource**，配置的random.*属性值
+ **jar包外部的application-{profile}.properties或application.yml(带spring.profile)配置文件**
+ **jar包内部的application-{profile}.properties或application.yml(带spring.profile)配置文件**
+ **jar包外部的application.properties或application.yml(不带spring.profile)配置文件**
+ **jar包内部的application.properties或application.yml(不带spring.profile)配置文件**
+ @Configuration注解类上的@PropertySource
+ 通过SpringApplication.setDefaultProperties指定的默认属性

### 1.6、自动配置原理

1）、SpringBoot启动的时候加载主配置类，开启了自动配置功能 ==@EnableAutoConfiguration==

2）、**@EnableAutoConfiguration 作用：**

- 利用EnableAutoConfigurationImportSelector给容器中导入一些组件

- 可以查看selectImports()方法的内容；

- List<String> configurations = getCandidateConfigurations(annotationMetadata,      attributes);获取候选的配置

  - ```java
    SpringFactoriesLoader.loadFactoryNames()
    扫描所有jar包类路径下  META-INF/spring.factories
    把扫描到的这些文件的内容包装成properties对象
    从properties中获取到EnableAutoConfiguration.class类（类名）对应的值，然后把他们添加在容器中
    ```

**将类路径下  META-INF/spring.factories 里面配置的所有EnableAutoConfiguration的值加入到了容器中**

每一个这样的  xxxAutoConfiguration类都是容器中的一个组件，都加入到容器中

3）、每一个自动配置类进行自动配置功能；

4）、以**HttpEncodingAutoConfiguration（Http编码自动配置）**为例解释自动配置原理

```java
@Configuration   //表示这是一个配置类，以前编写的配置文件一样，也可以给容器中添加组件
@EnableConfigurationProperties(HttpEncodingProperties.class)  //启动指定类的ConfigurationProperties功能；将配置文件中对应的值和HttpEncodingProperties绑定起来；并把HttpEncodingProperties加入到ioc容器中

@ConditionalOnWebApplication //Spring底层@Conditional注解（Spring注解版），根据不同的条件，如果满足指定的条件，整个配置类里面的配置就会生效；    判断当前应用是否是web应用，如果是，当前配置类生效

@ConditionalOnClass(CharacterEncodingFilter.class)  //判断当前项目有没有这个类CharacterEncodingFilter；SpringMVC中进行乱码解决的过滤器；

@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)  //判断配置文件中是否存在某个配置  spring.http.encoding.enabled；如果不存在，判断也是成立的
//即使我们配置文件中不配置pring.http.encoding.enabled=true，也是默认生效的；
public class HttpEncodingAutoConfiguration {
  
  	//他已经和SpringBoot的配置文件映射了
  	private final HttpEncodingProperties properties;
  
   //只有一个有参构造器的情况下，参数的值就会从容器中拿
  	public HttpEncodingAutoConfiguration(HttpEncodingProperties properties) {
		this.properties = properties;
	}
  
    @Bean   //给容器中添加一个组件，这个组件的某些值需要从properties中获取
	@ConditionalOnMissingBean(CharacterEncodingFilter.class) //判断容器没有这个组件？
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(this.properties.getCharset().name());
		filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
		filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
		return filter;
	}
```

​	    根据当前不同的条件判断，决定这个配置类是否生效。一但这个配置类生效，这个配置类就会给容器中添加各种组件，这些组件的属性是从对应的properties类中获取的，这些类里面的每一个属性又是和配置文件绑定的。

5）、所有在配置文件中能配置的属性都是在xxxxProperties类中被封装。配置文件能配置什么就可以参照某个功能对应的这个属性类

```java
@ConfigurationProperties(prefix = "spring.http.encoding")  //从配置文件中获取指定的值和bean的属性进行绑定
public class HttpEncodingProperties {
   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
```

**重点：**

​	**1）、SpringBoot启动会加载大量的自动配置类**

​	**2）、我们看我们需要的功能有没有SpringBoot默认写好的自动配置类**

​	**3）、我们再来看这个自动配置类中到底配置了哪些组件（只要我们要用的组件有，我们就不需要再来配置了）**

​	**4）、给容器中自动配置类添加组件的时候，会从properties类中获取某些属性。我们就可以在配置文件中指定这些属性的值**



xxxxAutoConfigurartion：自动配置类；

给容器中添加组件

xxxxProperties:封装配置文件中相关属性；

**@Conditional派生注解（Spring注解版原生的@Conditional作用）**

作用：必须是@Conditional指定的条件成立，才给容器中添加组件，配置配里面的所有内容才生效



| @Conditional扩展注解            | 作用（判断是否满足当前指定条件）                 |
| ------------------------------- | ------------------------------------------------ |
| @ConditionalOnJava              | 系统的java版本是否符合要求                       |
| @ConditionalOnBean              | 容器中存在指定Bean；                             |
| @ConditionalOnMissingBean       | 容器中不存在指定Bean；                           |
| @ConditionalOnExpression        | 满足SpEL表达式指定                               |
| @ConditionalOnClass             | 系统中有指定的类                                 |
| @ConditionalOnMissingClass      | 系统中没有指定的类                               |
| @ConditionalOnSingleCandidate   | 容器中只有一个指定的Bean，或者这个Bean是首选Bean |
| @ConditionalOnProperty          | 系统中指定的属性是否有指定的值                   |
| @ConditionalOnResource          | 类路径下是否存在指定资源文件                     |
| @ConditionalOnWebApplication    | 当前是web环境                                    |
| @ConditionalOnNotWebApplication | 当前不是web环境                                  |
| @ConditionalOnJndi              | JNDI存在指定项                                   |

**自动配置类必须在一定的条件下才能生效**，**通过启用  debug=true属性；来让控制台打印自动配置报告**

```java
============================
CONDITIONS EVALUATION REPORT
============================


Positive matches:// 启用的自动配置
-----------------

   GenericCacheConfiguration matched:
      - Cache org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration automatic cache type (CacheCondition)

   JmxAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.jmx.export.MBeanExporter' (OnClassCondition)
      - @ConditionalOnProperty (spring.jmx.enabled=true) matched (OnPropertyCondition)
 
          
Negative matches:// 未启用，没有匹配成功的自动配置类
-----------------

   ActiveMQAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'javax.jms.ConnectionFactory' (OnClassCondition)

   AopAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.aspectj.lang.annotation.Aspect' (OnClassCondition)

```

## 2、日志配置

市面上的日志组件：JUL、JCL、Jboss-logging、logback、log4j、log4j2、slf4j....



| 日志门面  （日志的抽象层）             | 日志实现                                             |
| -------------------------------------- | ---------------------------------------------------- |
| SLF4j(Simple  Logging Facade for Java) | Log4j  JUL（java.util.logging）  Log4j2  **Logback** |

**Spring框架默认使用JCL作为日志框架，SpringBoot默认使用用 SLF4j和logback**

系统如何配置SLF4J[都在这里](https://www.slf4j.org)

![](images/loggingconfig.png)

### 2.1、**系统的日志统一slf4j**

1、首先排除其他日志框架

2、使用slf4j提供的中间包替换排除的日志包

3、最后导入系统需要的slf4j的实现包

![](images/legacy.png)

### **2.2、Springboot日志关系**

![](images/loggingrel.png)

总结：

​	1）、SpringBoot底层也是使用slf4j+logback的方式进行日志记录

​	2）、SpringBoot也把其他的日志都替换成了slf4j

​	3）、中间替换包

​	4）、引入其他框架时，移除默认的日志依赖

**SpringBoot能自动适配所有的日志，而且底层使用slf4j+logback的方式记录日志，引入其他框架的时候，只需要把这个框架依赖的日志框架排除掉即可**

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### **2.3、springboot日志的默认配置**

日志的配置文件**spring-boot\2.1.5.RELEASE\spring-boot-2.1.5.RELEASE.jar\logging**

```properties
logging.level.com.atguigu=trace
#logging.path=
# 不指定路径在当前项目下生成springboot.log日志
# 可以指定完整的路径；
#logging.file=G:/springboot.log
# 在当前磁盘的根路径下创建spring文件夹和里面的log文件夹；使用 spring.log 作为默认文件
logging.path=/spring/log
#  在控制台输出的日志的格式
logging.pattern.console=%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n
# 指定文件中日志输出的格式
logging.pattern.file=%d{yyyy-MM-dd} === [%thread] === %-5level === %logger{50} ==== %msg%n
```

### **2.5、日志切换**（slf4j+log4j）

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  		<exclusions>
        <exclusion>
          <artifactId>logback-classic</artifactId>
          <groupId>ch.qos.logback</groupId>
        </exclusion>
        <exclusion>
          <artifactId>log4j-over-slf4j</artifactId>
          <groupId>org.slf4j</groupId>
        </exclusion>
     	</exclusions>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>spring-boot-starter-logging</artifactId>
            <groupId>org.springframework.boot</groupId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

## 3、web开发

### 3.1、SpringBoot对于静态资源的映射规则

```java
@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
public class ResourceProperties implements ResourceLoaderAware {
  // 可以设置和静态资源有关的参数，缓存时间等
```



```java
	WebMvcAuotConfiguration：
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (!this.resourceProperties.isAddMappings()) {
    	logger.debug("Default resource handling disabled");
    	return;
    }
    Integer cachePeriod = this.resourceProperties.getCachePeriod();
    if (!registry.hasMappingForPattern("/webjars/**")) {
    	customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
    			.addResourceLocations("classpath:/META-INF/resources/webjars/")
    			.setCachePeriod(cachePeriod));
    }
    String staticPathPattern = this.mvcProperties.getStaticPathPattern();
    // 静态资源文件夹映射
    if (!registry.hasMappingForPattern(staticPathPattern)) {
    			customizeResourceHandlerRegistration(
    			registry.addResourceHandler(staticPathPattern)
    			.addResourceLocations(this.resourceProperties.getStaticLocations())
    			.setCachePeriod(cachePeriod));
    }
}

// 配置欢迎页映射
@Bean
public WelcomePageHandlerMapping welcomePageHandlerMapping(
            ResourceProperties resourceProperties) {
            return new WelcomePageHandlerMapping(resourceProperties.getWelcomePage(),                                                 this.mvcProperties.getStaticPathPattern());
        }

// 配置喜欢的图标
@Configuration
@ConditionalOnProperty(value = "spring.mvc.favicon.enabled", matchIfMissing = true)
public static class FaviconConfiguration {

    private final ResourceProperties resourceProperties;
    public FaviconConfiguration(ResourceProperties resourceProperties) {
        this.resourceProperties = resourceProperties;
    }

    @Bean
    public SimpleUrlHandlerMapping faviconHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        //所有  **/favicon.ico 
        mapping.setUrlMap(Collections.singletonMap("**/favicon.ico",
                                                   faviconRequestHandler()));
        return mapping;
    }

    @Bean
    public ResourceHttpRequestHandler faviconRequestHandler() {
        ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
        requestHandler
            .setLocations(this.resourceProperties.getFaviconLocations());
        return requestHandler;
    }
}
```

**1）、所有 /webjars/** ，都去 classpath:/META-INF/resources/webjars/ 找资源**

​	[webjars：以jar包的方式引入静态资源](http://www.webjars.org/)

localhost:8080/webjars/jquery/3.3.1/jquery.js

```xml
<!--引入jquery-webjar-->在访问的时候只需要写webjars下面资源的名称即可
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>jquery</artifactId>
    <version>3.3.1</version>
</dependency>
```

**2）、"/" 访问当前项目的任何资源，都去（静态资源的文件夹）找映射**

```properties
"classpath:/META-INF/resources/", 
"classpath:/resources/",
"classpath:/static/", 
"classpath:/public/" 
"/"：当前项目的根路径
```

​	localhost:8080/abc   ---> 去静态资源文件夹找abc

**3）、欢迎页。** 静态资源文件夹下的所有index.html页面；被"/**"映射

​	localhost:8080/   找index页面

**4）、所有的** **/favicon.ico  都是在静态资源文件下找

### 3.2、Thymeleaf模版引擎

pom引入模版

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
    2.1.6
</dependency>
<!-- 切换thymeleaf版本 -->
<properties>
		<thymeleaf.version>3.0.9.RELEASE</thymeleaf.version>
		<!-- 布局功能的支持程序  thymeleaf3主程序  layout2以上版本 -->
		<!-- thymeleaf2   layout1-->
		<thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
  </properties>
```

Thymeleaf在SpringBoot中的默认配置

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {
	private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");
	private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");
	public static final String DEFAULT_PREFIX = "classpath:/templates/";
	public static final String DEFAULT_SUFFIX = ".html";
```

Thymeleaf在html的使用

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"> <!-- 命名空间提示作用 -->
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1>成功！</h1>
    <!--th:text 将div里面的文本内容设置为 -->
    <div th:text="${hello}">这是显示欢迎信息</div>
</body>
</html>
```

### 3.3、Thymeleaf语法规则

[官方文档](<https://www.thymeleaf.org/documentation.html>)

1）、th:text	改变当前元素里面的文本内容

​		  th：任意html属性	来替换原生属性的值

![](images/thymeleafoperation.png)



2）、表达式

```proper
Simple expressions:（表达式语法）
    Variable Expressions: ${...}：获取变量值；OGNL；
    		1）、获取对象的属性、调用方法
    		2）、使用内置的基本对象：
    			#ctx : the context object.
    			#vars: the context variables.
                #locale : the context locale.
                #request : (only in Web Contexts) the HttpServletRequest object.
                #response : (only in Web Contexts) the HttpServletResponse object.
                #session : (only in Web Contexts) the HttpSession object.
                #servletContext : (only in Web Contexts) the ServletContext object.
                
                ${session.foo}
            3）、内置的一些工具对象：
#execInfo : information about the template being processed.
#messages : methods for obtaining externalized messages inside variables expressions, in the same way as they would be obtained using #{…} syntax.
#uris : methods for escaping parts of URLs/URIs
#conversions : methods for executing the configured conversion service (if any).
#dates : methods for java.util.Date objects: formatting, component extraction, etc.
#calendars : analogous to #dates , but for java.util.Calendar objects.
#numbers : methods for formatting numeric objects.
#strings : methods for String objects: contains, startsWith, prepending/appending, etc.
#objects : methods for objects in general.
#bools : methods for boolean evaluation.
#arrays : methods for arrays.
#lists : methods for lists.
#sets : methods for sets.
#maps : methods for maps.
#aggregates : methods for creating aggregates on arrays or collections.
#ids : methods for dealing with id attributes that might be repeated (for example, as a result of an iteration).

    Selection Variable Expressions: *{...}：选择表达式：和${}在功能上是一样；
    	补充：配合 th:object="${session.user}：
   <div th:object="${session.user}">
    <p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
    <p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
    <p>Nationality: <span th:text="*{nationality}">Saturn</span>.</p>
    </div>
    
    Message Expressions: #{...}：获取国际化内容
    Link URL Expressions: @{...}：定义URL；
    		@{/order/process(execId=${execId},execType='FAST')}
    Fragment Expressions: ~{...}：片段引用表达式
    		<div th:insert="~{commons :: main}">...</div>
    		
Literals（字面量）
      Text literals: 'one text' , 'Another one!' ,…
      Number literals: 0 , 34 , 3.0 , 12.3 ,…
      Boolean literals: true , false
      Null literal: null
      Literal tokens: one , sometext , main ,…
Text operations:（文本操作）
    String concatenation: +
    Literal substitutions: |The name is ${name}|
Arithmetic operations:（数学运算）
    Binary operators: + , - , * , / , %
    Minus sign (unary operator): -
Boolean operations:（布尔运算）
    Binary operators: and , or
    Boolean negation (unary operator): ! , not
Comparisons and equality:（比较运算）
    Comparators: > , < , >= , <= ( gt , lt , ge , le )
    Equality operators: == , != ( eq , ne )
Conditional operators:条件运算（三元运算符）
    If-then: (if) ? (then)
    If-then-else: (if) ? (then) : (else)
    Default: (value) ?: (defaultvalue)
Special tokens:
    No-Operation: _ 
```

### 3.4、SpringMVC自动配置

Spring Boot 自动配置好了SpringMVC

以下是SpringBoot对SpringMVC的默认配置:**（WebMvcAutoConfiguration）**

```java
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@ConditionalOnMissingBean({WebMvcConfigurationSupport.class})
@AutoConfigureOrder(-2147483638)
@AutoConfigureAfter({DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class, ValidationAutoConfiguration.class})
public class WebMvcAutoConfiguration {
```



- Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans.

  - 自动配置了ViewResolver（视图解析器：根据方法的返回值得到视图对象（View），视图对象决定如何渲染（转发？重定向？））
  - ContentNegotiatingViewResolver：组合所有的视图解析器
  - 如何定制：**可以向容器中添加一个视图解析器，Spring会自动的将其组合进来**


**ContentNegotiatingViewResolver**分析

```java
@Bean	// 把ContentNegotiatingViewResolver交给Spring容器管理
@ConditionalOnBean({ViewResolver.class})
@ConditionalOnMissingBean(name = {"viewResolver"},value = {ContentNegotiatingViewResolver.class})
public ContentNegotiatingViewResolver viewResolver(BeanFactory beanFactory) {
	ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
resolver.setContentNegotiationManager((ContentNegotiationManager)beanFactory.getBean(ContentNegotiationManager.class));
    resolver.setOrder(-2147483648);
    return resolver;
}
```

```java
@Override
@Nullable
public View resolveViewName(String viewName, Locale locale) throws Exception {
    RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
	...
    if (requestedMediaTypes != null) {
// 获取所有的视图对象
List<View> candidateViews = getCandidateViews(viewName, locale, requestedMediaTypes);
// 将最好的视图对象返回
View bestView = getBestView(candidateViews, requestedMediaTypes, attrs);
	...
}
```

```java
private List<View> getCandidateViews(String viewName, Locale locale, List<MediaType> requestedMediaTypes) throws Exception {
	List<View> candidateViews = new ArrayList<>();
		...
	for (ViewResolver viewResolver : this.viewResolvers) {
        View view = viewResolver.resolveViewName(viewName, locale);
        if (view != null) {
            candidateViews.add(view);// 拿所有存在容器中的view对象
        }
        for (MediaType requestedMediaType : requestedMediaTypes) {
            List<String> extensions = this.contentNegotiationManager.resolveFileExtensions(requestedMediaType);
            for (String extension : extensions) {
                String viewNameWithExtension = viewName + '.' + extension;
                view = viewResolver.resolveViewName(viewNameWithExtension, locale);
                if (view != null) {
                    candidateViews.add(view);
                }
            }
        }
    }
    .....
}
```

```java
@Override
protected void initServletContext(ServletContext servletContext) {
    Collection<ViewResolver> matchingBeans =
        BeanFactoryUtils.beansOfTypeIncludingAncestors(obtainApplicationContext(), ViewResolver.class).values();	// 获取所有的视图解析器
    if (this.viewResolvers == null) {
        this.viewResolvers = new ArrayList<>(matchingBeans.size());
        for (ViewResolver viewResolver : matchingBeans) {
            if (this != viewResolver) {
                this.viewResolvers.add(viewResolver);
            }
        }
    }
    else {
        for (int i = 0; i < this.viewResolvers.size(); i++) {
            ViewResolver vr = this.viewResolvers.get(i);
            if (matchingBeans.contains(vr)) {
                continue;
            }
            String name = vr.getClass().getName() + i;
            obtainApplicationContext().getAutowireCapableBeanFactory().initializeBean(vr, name);
        }

    }
    if (this.viewResolvers.isEmpty()) {
        logger.warn("Did not find any ViewResolvers to delegate to; please configure them using the " +
                    "'viewResolvers' property on the ContentNegotiatingViewResolver");
    }
    AnnotationAwareOrderComparator.sort(this.viewResolvers);
    this.cnmFactoryBean.setServletContext(servletContext);
}
```

**BeanNameViewResolver解析**

```java
@Bean
@ConditionalOnBean({View.class})
@ConditionalOnMissingBean
public BeanNameViewResolver beanNameViewResolver() {
    BeanNameViewResolver resolver = new BeanNameViewResolver();
    resolver.setOrder(2147483637);
    return resolver;
}
```

```java
public View resolveViewName(String viewName, Locale locale) throws BeansException {
		ApplicationContext context = obtainApplicationContext();
		if (!context.containsBean(viewName)) {
			if (logger.isDebugEnabled()) {
				logger.debug("No matching bean found for view name '" + viewName + "'");
			}
			// Allow for ViewResolver chaining...
			return null;
		}
		if (!context.isTypeMatch(viewName, View.class)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Found matching bean for view name '" + viewName +
						"' - to be ignored since it does not implement View");
			}
			// Since we're looking into the general ApplicationContext here,
			// let's accept this as a non-match and allow for chaining as well...
			return null;
		}
		return context.getBean(viewName, View.class);
	}
```



- Support for serving static resources, including support for WebJars (see below)

  - 静态资源文件夹路径，webjars

- Static `index.html` support. 静态首页访问

- Custom `Favicon` support (see below).  favicon.ico

- 自动注册了 of `Converter`, `GenericConverter`, `Formatter` beans

  ```java
  public void addFormatters(FormatterRegistry registry) {
      // 拿到所有的convert
      Iterator var2 = this.getBeansOfType(Converter.class).iterator();
      while(var2.hasNext()) {
          Converter<?, ?> converter = (Converter)var2.next();
          registry.addConverter(converter);
      }
     // 拿到所有的GenericConverter
      var2 = this.getBeansOfType(GenericConverter.class).iterator();
      while(var2.hasNext()) {
          GenericConverter converter = (GenericConverter)var2.next();
          registry.addConverter(converter);
      }
  	// get all Formatter
      var2 = this.getBeansOfType(Formatter.class).iterator();
      while(var2.hasNext()) {
          Formatter<?> formatter = (Formatter)var2.next();
          registry.addFormatter(formatter);
      }
  }
  ```

  - Converter：转换器；  public String hello(User user)：类型转换使用Converter
  - `Formatter`  格式化器；  2017.12.17===Date；

```java
@Bean
@ConditionalOnProperty(prefix = "spring.mvc", name = "date-format")//在文件中配置日期格式化的规则
public Formatter<Date> dateFormatter() {
    return new DateFormatter(this.mvcProperties.getDateFormat());//日期格式化组件
}
```

**自定义的格式化器转换器，只需放在容器中即可**

- Support for `HttpMessageConverters` (see below)

  - HttpMessageConverter：SpringMVC用来转换Http请求和响应的；User---Json

  - `HttpMessageConverters` 是从容器中确定；获取所有的HttpMessageConverter

  - **自己给容器中添加HttpMessageConverter，只需要将自己的组件注册容器中（@Bean,@Component）**

- Automatic registration of `MessageCodesResolver` (see below).定义错误代码生成规则

- Automatic use of a `ConfigurableWebBindingInitializer` bean (see below).

  **我们可以配置一个ConfigurableWebBindingInitializer来替换默认的（添加到容器）**

  ```
  初始化WebDataBinder；
  请求数据=====JavaBean；
  ```

**org.springframework.boot.autoconfigure.web：web的所有自动场景；**

If you want to keep Spring Boot MVC features, and you just want to add additional [MVC configuration](https://docs.spring.io/spring/docs/4.3.14.RELEASE/spring-framework-reference/htmlsingle#mvc) (interceptors, formatters, view controllers etc.) you can add your own `@Configuration` class of type `WebMvcConfigurerAdapter`, but **without** `@EnableWebMvc`. If you wish to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter` or `ExceptionHandlerExceptionResolver` you can declare a `WebMvcRegistrationsAdapter` instance providing such components.

If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`.

### 3.5、扩展SpringMVC

**编写一个配置类（@Configuration），是WebMvcConfigurerAdapter类型；不能标注@EnableWebMvc**

```java
//使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       // super.addViewControllers(registry);
        //浏览器发送 /atguigu 请求来到 success
        registry.addViewController("/atguigu").setViewName("success");
    }
}
```

原理：

​	1）、WebMvcAutoConfiguration是SpringMVC的自动配置类

​	2）、在做其他自动配置时会导入；@Import(**EnableWebMvcConfiguration**.class)

​	3）、容器中所有的WebMvcConfigurer都会一起起作用

​	4）、我们的配置类也会被调用

​	效果：SpringMVC的自动配置和我们的扩展配置都会起作用

```java
@Configuration
public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration {
    private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();

    //从容器中获取所有的WebMvcConfigurer
    @Autowired(required = false)
    public void setConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addWebMvcConfigurers(configurers);
            //一个参考实现；将所有的WebMvcConfigurer相关配置都来一起调用；  
            @Override
            // public void addViewControllers(ViewControllerRegistry registry) {
            //    for (WebMvcConfigurer delegate : this.delegates) {
            //       delegate.addViewControllers(registry);
            //   }
        }
    }
}
```

### 3.6、全面接管SpringMVC

**只需在配置类中添加@EnableWebMvc即可**

```JAVA
//使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
@EnableWebMvc
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       // super.addViewControllers(registry);
        //浏览器发送 /atguigu 请求来到 success
        registry.addViewController("/atguigu").setViewName("success");
    }
}
```

原理：@EnableWebMvc

```java
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
```

**DelegatingWebMvcConfiguration**类

```java
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
```

**WebMvcConfigurationSupport**类

```java
// 容器中没有这个组件的时候，这个自动配置类才生效。当我们配置了@EnableWebMvc时，容器中就存在了WebMvcConfigurationSupport，这个自动配置类就不会生效了
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
```

4）、@**EnableWebMvc**将WebMvcConfigurationSupport组件导入进来

5）、导入的WebMvcConfigurationSupport只是SpringMVC最基本的功能