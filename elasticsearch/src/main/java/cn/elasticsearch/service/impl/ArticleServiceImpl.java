package cn.elasticsearch.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.elasticsearch.dao.ArticleRepository;
import cn.elasticsearch.domain.Article;
import cn.elasticsearch.service.ArticleService;
@Service
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleRepository articleRepository;
	public void save(Article article){
		articleRepository.save(article);
	}
	public void delete(Article article) {
		articleRepository.delete(article);
	}
	public Article findOne(Article article) {
		
		return articleRepository.findOne(article.getId());
	}
	public Iterable<Article> findAll() {
		
		return articleRepository.findAll();
	}
	public Page<Article> findAll(Pageable pageable) {
		
		return articleRepository.findAll(pageable);
	}
	public Page<Article> findByTitle(String title, Pageable pageable) {
		
		return articleRepository.findByTitle(title,pageable);
	}
	
}
