package cn.elasticsearch.test;

import java.util.Iterator;

import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.elasticsearch.domain.Article;
import cn.elasticsearch.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ElasticSearchTest {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private Client client;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Test
	public void createIndex(){
		elasticsearchTemplate.createIndex(Article.class);  //创建索引
		elasticsearchTemplate.putMapping(Article.class);   //建立映射关系
	}
	
	@Test
	public void save(){
		for (int i = 1; i < 101; i++) {
			Article article = new Article();
			article.setId(i);
			article.setTitle(i+":测试spring data elasticsearch");
			article.setContext(i+":进球gif-权健连续配合眼花缭乱 赵旭日梅开二度");
			articleService.save(article);
		}
	}
	
	@Test
	public void delete(){
		Article article = new Article();
		article.setId(1001);
		articleService.delete(article);
	}
	
	@Test
	public void findOne(){
		Article article = new Article();
		article.setId(1);
		Article _article = articleService.findOne(article);
		System.out.println(_article);
	}
	//查询全部，未分页
	@Test
	public void findAll_1(){
		Iterable<Article> iterable = articleService.findAll();
		Iterator<Article> iterator = iterable.iterator();
		//注释方式是错误的 ！！！
		/*while(iterable.iterator().hasNext()){
			Article article = iterable.iterator().next();
			System.out.println(article);
		}*/
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
	}
	//分页查询
	@Test
	public void findAll(){
		int count = 0;
		Pageable pageable = new PageRequest(1, 20);
		Page<Article> pageData = articleService.findAll(pageable);
		for(Article article:pageData.getContent()){
			count++;
			System.out.println(article);
		}
		System.out.println("count:"+count);
	}
	//分页查询
		@Test
		public void findAll_add(){
			int count = 0;
			Pageable pageable = new PageRequest(1, 20);
			Page<Article> pageData = articleService.findByTitle("测试",pageable);
			for(Article article:pageData.getContent()){
				count++;
				System.out.println(article);
			}
			System.out.println("count:"+count);
		}
}
