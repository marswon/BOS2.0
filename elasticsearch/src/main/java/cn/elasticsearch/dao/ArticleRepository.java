package cn.elasticsearch.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cn.elasticsearch.domain.Article;



public interface ArticleRepository extends ElasticsearchRepository<Article, Integer> {

	Page<Article> findByTitle(String title, Pageable pageable);

}
