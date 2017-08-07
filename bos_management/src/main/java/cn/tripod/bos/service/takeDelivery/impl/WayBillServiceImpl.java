package cn.tripod.bos.service.takeDelivery.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.takeDelivery.WayBillRepository;
import cn.tripod.bos.domain.takeDelivery.WayBill;
import cn.tripod.bos.indexdao.WayBillIndexRepository;
import cn.tripod.bos.service.takeDelivery.WayBillService;
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	@Autowired
	private Client client;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Override
	public void save(WayBill model) {
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
		if(persistWayBill==null){
			wayBillRepository.save(model);
			wayBillIndexRepository.save(model);
		}else{
			//将持久态对象属性的值全部同步给瞬时态
			model.setId(persistWayBill.getId());
			BeanUtils.copyProperties(model, persistWayBill);
//			BeanUtils.copyProperties(persistWayBill, model);
//			wayBillRepository.save(model);
			wayBillIndexRepository.save(persistWayBill);
		}
	}
	//elasticsearch全文检索
	@Override
	public Page<WayBill> findAll(WayBill wayBill, Pageable pageable) {
		//无条件时
		if(StringUtils.isBlank(wayBill.getWayBillNum())
				&&StringUtils.isBlank(wayBill.getSendAddress())
				&&StringUtils.isBlank(wayBill.getRecAddress())
				&&StringUtils.isBlank(wayBill.getSendProNum())
				&&(wayBill.getSignStatus()!=null||wayBill.getSignStatus()==0)){
			
			return wayBillRepository.findAll(pageable);
		}else{
			/*查询条件：
			 * must:条件必须成立
			 * mustNot:条件必须不成立
			 * should:条件可以成立
			 */
			//布尔查询   用来综合查询条件
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
			//等值查询
			if(StringUtils.isNoneBlank(wayBill.getWayBillNum())){
				QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				boolQueryBuilder.must(queryBuilder);
			}
			//模糊查询
			if(StringUtils.isNoneBlank(wayBill.getSendAddress())){
				QueryBuilder queryBuilder = new WildcardQueryBuilder("sendAddress","*"+ wayBill.getSendAddress()+"*");
				//支持多个分词组合查询
				QueryBuilder queryBuilder2 = new QueryStringQueryBuilder(wayBill.getSendAddress())
					.field("sendAddress").defaultOperator(Operator.AND);
				BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();
				boolQueryBuilder2.should(queryBuilder);
				boolQueryBuilder2.should(queryBuilder2);
				boolQueryBuilder.must(boolQueryBuilder2);
			}
			//模糊查询
			if(StringUtils.isNoneBlank(wayBill.getRecAddress())){
				QueryBuilder queryBuilder = new WildcardQueryBuilder("recAddress", "*"+wayBill.getRecAddress()+"*");
				boolQueryBuilder.must(queryBuilder);
			}
			//等值查询
			if(StringUtils.isNoneBlank(wayBill.getSendProNum())){
				QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				boolQueryBuilder.must(queryBuilder);
			}
			//等值查询
			if((wayBill.getSignStatus()!=null&&wayBill.getSignStatus()!=0)){
				QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				boolQueryBuilder.must(queryBuilder);
			}
			//将布尔查询综合的条件赋值给查询searchQuery对象
			SearchQuery searchQuery = new NativeSearchQuery(boolQueryBuilder);
			searchQuery.setPageable(pageable);//分页效果
			return wayBillIndexRepository.search(searchQuery);
			
		}
	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
	@Override
	public List<WayBill> findAll(WayBill wayBill) {
		
		//无条件时
		if(StringUtils.isBlank(wayBill.getWayBillNum())
				&&StringUtils.isBlank(wayBill.getSendAddress())
				&&StringUtils.isBlank(wayBill.getRecAddress())
				&&StringUtils.isBlank(wayBill.getSendProNum())
				&&(wayBill.getSignStatus()==null||wayBill.getSignStatus()==0)){
			
			return wayBillRepository.findAll();
		}else{
			/*查询条件：
			 * must:条件必须成立
			 * mustNot:条件必须不成立
			 * should:条件可以成立
			 */
			//布尔查询   用来综合查询条件
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
			//等值查询
			if(StringUtils.isNoneBlank(wayBill.getWayBillNum())){
				QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				boolQueryBuilder.must(queryBuilder);
			}
			//模糊查询
			if(StringUtils.isNoneBlank(wayBill.getSendAddress())){
				QueryBuilder queryBuilder = new WildcardQueryBuilder("sendAddress","*"+ wayBill.getSendAddress()+"*");
				//支持多个分词组合查询
				QueryBuilder queryBuilder2 = new QueryStringQueryBuilder(wayBill.getSendAddress())
					.field("sendAddress").defaultOperator(Operator.AND);
				BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();
				boolQueryBuilder2.should(queryBuilder);
				boolQueryBuilder2.should(queryBuilder2);
				boolQueryBuilder.must(boolQueryBuilder2);
			}
			//模糊查询
			if(StringUtils.isNoneBlank(wayBill.getRecAddress())){
				QueryBuilder queryBuilder = new WildcardQueryBuilder("recAddress", "*"+wayBill.getRecAddress()+"*");
				boolQueryBuilder.must(queryBuilder);
			}
			//等值查询
			if(StringUtils.isNoneBlank(wayBill.getSendProNum())){
				QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				boolQueryBuilder.must(queryBuilder);
			}
			//等值查询
			if((wayBill.getSignStatus()!=null&&wayBill.getSignStatus()!=0)){
				QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				boolQueryBuilder.must(queryBuilder);
			}
			//将布尔查询综合的条件赋值给查询searchQuery对象
			SearchQuery searchQuery = new NativeSearchQuery(boolQueryBuilder);
			//elasticsearch分页查询允许的最大记录条数为10000
			Pageable pageable = new PageRequest(0, 10000);
			searchQuery.setPageable(pageable);//分页效果
			return wayBillIndexRepository.search(searchQuery).getContent();		
		}
	}
	
}
