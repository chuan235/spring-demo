package com.gc.elasticsearch.dao;

import com.gc.elasticsearch.bean.StoryBook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRespository extends ElasticsearchRepository<StoryBook,Integer> {
}
