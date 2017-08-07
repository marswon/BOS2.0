package cn.tripod.bos;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class Jedis_Test {
	@Test
	public void test1(){
		Jedis jedis = new Jedis("localhost");
		String setex = jedis.setex("company", 30, "tripod");   //向redis中存值，且有效时间30秒
		System.out.println(jedis.get("company"));		//取值
	}
}
