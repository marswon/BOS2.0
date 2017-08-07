package cn.tripod.bos.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.tripod.bos.dao.base.StandardRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class StandardRepositoryTest {
	@Autowired
	private StandardRepository standardRepository;
	@Test
	public void test1(){
		System.out.println(standardRepository.findByName("10-20"));
	}
	
	@Test
	public void test2(){
		System.out.println(standardRepository.queryName("10-20"));
	}
}
