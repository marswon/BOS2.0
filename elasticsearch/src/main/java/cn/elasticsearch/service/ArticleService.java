package cn.elasticsearch.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.elasticsearch.domain.Article;

public interface ArticleService {
	public void save(Article article);
	public void delete(Article article);
	public Article findOne(Article article);
	public Iterable<Article> findAll();
	public Page<Article> findAll(Pageable pageable);
	public Page<Article> findByTitle(String title, Pageable pageable);
}
