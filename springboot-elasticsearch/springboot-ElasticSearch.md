#### 配置Elasticsearch的开发环境

1、Elasticsearch对应的spring-data-elasticsearch

| Spring Data Elasticsearch | Elasticsearch |
| ------------------------- | ------------- |
| 3.2.x                     | 6.7.2         |
| 3.1.x                     | 6.2.2         |
| 3.0.x                     | 5.5.0         |
| 2.1.x                     | 2.4.0         |
| 2.0.x                     | 2.2.0         |
| 1.3.x                     | 1.5.2         |

2、为自己的Elasticsearch选择对应的Spring Data Elasticsearch依赖elasticSearch

```shell
elasticsearch       5.6.13              3fd2f723b598        7 months ago        486MB
```

```xml
<dependency>      
	<groupId>org.springframework.data</groupId>
    <artifactId>spring-data-elasticsearch</artifactId>
    <version>3.0.13.RELEASE</version>
</dependency>
```

3、测试REST ful接口

```json
#搜索 last_name=Smith
{
    "query" : {
        "match" : {
            "last_name" : "Smith"
        }
    }
}
#复杂搜索 last_name=smmith and age>30
{
    "query" : {
        "bool": {
            "must": {
                "match" : {
                    "last_name" : "smith"
                }
            },
            "filter": {
                "range" : {
                    "age" : { "gt" : 30 }
                }
            }
        }
    }
}
# 全文搜索，按相关度进行排序
{
    "query" : {
        "match" : {
            "about" : "rock climbing"
        }
    }
}
# 短语搜索rock climbing
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    }
}
# 高亮搜索 rock和climbing高亮
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    },
    "highlight": {
        "fields" : {
            "about" : {}
        }
    }
}

```

Elasticsearch的数据分析

```json
# 数据分析 找出雇员中最受欢迎的兴趣爱好
{
  "aggs": {
    "all_interests": {
      "terms": { "field": "interests" }
    }
  }
}
"all_interests": { # 结果 两位员工对音乐感兴趣 一位对林地感兴趣，一位对运动感兴趣
    "buckets": [
        {
            "key":       "music",
            "doc_count": 2
        },
        {
            "key":       "forestry",
            "doc_count": 1
        },
        {
            "key":       "sports",
            "doc_count": 1
        }
    ]
}
```

```json
{#查询名为Smith雇员最受欢迎的兴趣爱好
  "query": {
    "match": {
      "last_name": "smith"
    }
  },
  "aggs": {
    "all_interests": {
      "terms": {
        "field": "interests"
      }
    }
  }
}
"all_interests": {#结果包含匹配查询的文档
     "buckets": [
        {
           "key": "music",
           "doc_count": 2
        },
        {
           "key": "sports",
           "doc_count": 1
        }
     ]
  }
```

4、使用JAVA API操作Elasticsearch

+ 使用Jest工具包操作Elasticsearch
+ spring-data-elasticsearch **注意版本**

（1）使用Jest工具包操作Elasticsearch

1、导入jest依赖

```xml
<dependency>
    <groupId>io.searchbox</groupId>
    <artifactId>jest</artifactId>
    <version>5.3.3</version>
</dependency>
```

2、配置es的地址

```properties
spring.elasticsearch.jest.uris=http://192.168.222.128:9200/
```

3、书写测试代码

```java
// 实体类中可以使用@JestId注解表示主键
public class News {
    @JestId
    private Integer id;
    private String content;
}
@Autowired
JestClient jestClient;
	// 插入操作
    News news = new News(3, "新闻消息");
    Index index = new Index.Builder(news).index("newindex").type("news").build();
    jestClient.execute(index);
	// 查询操作
	Get get = new Get.Builder("newindex", "3").type("news").build();
	JestResult result = jestClient.execute(get);
	News news = result.getSourceAsObject(News.class);
	System.out.println(news);
```

更过操作参考：https://github.com/searchbox-io/Jest

（2）使用spring-data-elasticsearch操作Elasticsearch

注意的几点：

+ elasticsearch从5版本以后默认不开启远程连接，需要修改配置文件
+ spring-data-elasticsearch需要适配对应的elasticsearch版本，对应关系见本文首页
+ 有的spring-data-elasticsearch版本对应TransportClient是一个接口，需要手动配置TransportClient的ip地址和端口

1、导入jar包

```xml
<!--内部使用的spring-data-elasticsearch是3.1.9，对应的TransportClient是一个抽象类，可以在properties里配置
	3.0.12版本对应的TransportClient就是一个接口--->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    <version>2.1.1.RELEASE</version>
</dependency>
```

2、基本配置

```properties
spring.data.elasticsearch.cluster-name=elasticsearch
# 9300端口用于测试没修改配置文件的es，9301对应修改了配置文件的es
spring.data.elasticsearch.cluster-nodes=192.168.222.128:9301
```

3、书写实体类

```java
@Document(indexName = "test", type = "book", shards = 1, replicas = 0, refreshInterval = "-1")
public class StoryBook {

    /**
     * Id注解加上后，在Elasticsearch里相应于该列就是主键了，在查询时就可以直接用主键查询
     *
     */
    @Id
    private Integer id;
```

4、书写数据操作接口

```java
public interface BookRespository extends ElasticsearchRepository<StoryBook,Integer> {
}
```

5、开始测试

```java
@Test
public void contextLoads() {
    StoryBook book = new StoryBook("西游记","吴承恩");
    book.setId(2);
    bookRepository.save(book);
}
// 通过url访问能查询到数据说明测试通过：http://192.168.222.128:9201/test/book/2
// {"_index":"test","_type":"book","_id":"2","_version":1,"found":true,"_source":{"id":2,"bookName":"西游记","author":"吴承恩"}}
```

更多操作查看：https://github.com/spring-projects/spring-data-elasticsearch

#### 配置文件的修改

1、进入容器内部，查看配置文件地址

```shell
docker exec -it ES02 /bin/bash
```

2、拷贝并修改配置文件

```shell
docker cp ES02:/usr/share/elasticsearch/config/elasticsearch.yml /root/conf/
vi /root/conf/elasticsearch.yml

http.host: 0.0.0.0
# 将transport.host的注释打开
transport.host: 0.0.0.0
```

3、重新启动容器

```shell
docker run -e ES_JAVA_OPTS="-Xms256m -Xmx256m" -d -p 9201:9200 -p 9301:9300 -v /root/conf/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml --name myes 3fd2f723b598
```

4、解决容器无法启动的问题

```shell
vi /etc/security/limits.conf
# 添加下面的内容 nofile是单个进程允许打开的最大文件个数 soft nofile 是软限制 hard nofile是硬限制
* soft nofile 65536
* hard nofile 65536

vi /etc/sysctl.conf
# 添加下面的内容
vm.max_map_count=655360

# 执行如下命令，使修改的参数生效
sysctl -p
```

5、重启容器，如果重启容器失败，可以重启docker，再者重启虚拟机



#### 配置TransportClient客户端

书写一个配置类，为容器注入这个对象。

```java
@Configuration
public class ClientConfig {

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        TransportClient client = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build())
                .addTransportAddress(new InetSocketTransportAddress(
                        InetAddress.getByName("docker"), 9301));
        return client;
    }
}
```



