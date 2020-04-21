package com.gc.elasticsearch;

import com.gc.elasticsearch.bean.News;
import com.gc.elasticsearch.bean.StoryBook;
import com.gc.elasticsearch.dao.BookRespository;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.Transport;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTests {

    @Autowired
    JestClient jestClient;

    @Autowired
    BookRespository bookRepository;

    @Autowired
    ElasticsearchTemplate template;

    @Test
    public void contextLoads() {

        StoryBook book = new StoryBook("西游记","吴承恩");
        book.setId(2);
        bookRepository.save(book);
    }

    @Test
    public void testJest(){
        News news = new News(3, "新闻消息");
        Index index = new Index.Builder(news).index("newindex").type("news").build();
        try {
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("插入完成");
    }

    @Test
    public void getData(){
        Get get = new Get.Builder("newindex", "3").type("news").build();
        try {
            JestResult result = jestClient.execute(get);
            News news = result.getSourceAsObject(News.class);
            System.out.println(news);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("插入完成");
    }
}
