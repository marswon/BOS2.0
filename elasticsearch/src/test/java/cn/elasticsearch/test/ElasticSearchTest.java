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
		elasticsearchTemplate.createIndex(Article.class);  //��������
		elasticsearchTemplate.putMapping(Article.class);   //����ӳ���ϵ
	}
	
	@Test
	public void save(){
		for (int i = 1; i < 101; i++) {
			Article article = new Article();
			article.setId(i);
			article.setTitle(i+":����spring data elasticsearch");
			article.setContext(i+":����gif-Ȩ����������ۻ����� ������÷������");
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
	//��ѯȫ����δ��ҳ
	@Test
	public void findAll_1(){
		Iterable<Article> iterable = articleService.findAll();
		Iterator<Article> iterator = iterable.iterator();
		//ע�ͷ�ʽ�Ǵ���� ������
		/*while(iterable.iterator().hasNext()){
			Article article = iterable.iterator().next();
			System.out.println(article);
		}*/
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
	}
	//��ҳ��ѯ
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
	//��ҳ��ѯ
		@Test
		public void findAll_add(){
			int count = 0;
			Pageable pageable = new PageRequest(1, 20);
			Page<Article> pageData = articleService.findByTitle("����",pageable);
			for(Article article:pageData.getContent()){
				count++;
				System.out.println(article);
			}
			System.out.println("count:"+count);
		}
}
