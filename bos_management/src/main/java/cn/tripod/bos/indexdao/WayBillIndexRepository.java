package cn.tripod.bos.indexdao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cn.tripod.bos.domain.takeDelivery.WayBill;

public interface WayBillIndexRepository extends 
	
	ElasticsearchRepository<WayBill, Integer> {

}
