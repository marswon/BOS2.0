package cn.tripod.bos.service.takeDelivery.impl;


import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.base.AreaDao;
import cn.tripod.bos.dao.base.FixedAreaDao;
import cn.tripod.bos.dao.takeDelivery.OrderRepository;
import cn.tripod.bos.domain.base.Area;
import cn.tripod.bos.domain.base.Courier;
import cn.tripod.bos.domain.base.FixedArea;
import cn.tripod.bos.domain.base.SubArea;
import cn.tripod.bos.domain.takeDelivery.Constant;
import cn.tripod.bos.domain.takeDelivery.Order;
import cn.tripod.bos.domain.takeDelivery.WorkBill;
import cn.tripod.bos.service.takeDelivery.OrderService;
import cn.tripod.bos.service.takeDelivery.WorkBillService;
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private FixedAreaDao fixedAreaDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private WorkBillService workBillService;
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsQueueTemplate;
	@Override
	public void addOrder(Order order) {
		
		order.setOrderNum(UUID.randomUUID().toString());//订单号
		order.setOrderTime(new Date());//下单时间
		order.setStatus("1");//订单状态--“待取件”
		
		// 寄件人 省市区
		Area area = order.getSendArea();
		Area persistArea = areaDao.findByProvinceAndCityAndDistrict(
				area.getProvince(), area.getCity(), area.getDistrict());
		// 收件人 省市区
		Area recArea = order.getSendArea();
		Area persistRecArea = areaDao
				.findByProvinceAndCityAndDistrict(recArea.getProvince(),
						recArea.getCity(), recArea.getDistrict());
		order.setSendArea(persistArea);
		order.setRecArea(persistRecArea);

		//自动分单：基于Customer地址库匹配定区   再根据定区匹配快递员
		String fixedAreaId = WebClient.create(Constant.CRM_MANAGEMENT_URL
				+"/services/customerService/findfixedAreaIdByAddress"
				+ "?sendAddress="+order.getSendAddress())
			.accept(MediaType.APPLICATION_JSON)
			.get(String.class);
		System.err.println(order);
		System.err.println("fixedAreaId:"+fixedAreaId);
		if(fixedAreaId!= null){
			//若能匹配定区
			FixedArea fixedArea = fixedAreaDao.findOne(fixedAreaId);
			//根据定区获取快递员
			Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
			while(iterator.hasNext()){
				Courier courier = iterator.next();
				if(courier!=null){
					order.setCourier(courier);  //关联快递员
					orderRepository.save(order);
					System.err.println("(基于Customer),自动分单成功......");
					//生成工单
					generateWorkBill(order);
					System.out.println("(基于Customer)工单生成成功，且短信通知...");
					return;
				}
			}
		}else{
			//根据订单省市区匹配分区 
			Set<SubArea> subareas = persistArea.getSubareas();
			for (SubArea subArea : subareas) {
				//若当前下单地址包含分区关键字或分区辅助关键字
				if(order.getSendAddress().contains(subArea.getKeyWords())||order.getSendAddress().contains(subArea.getAssistKeyWords())){
					//根据定区获取快递员
					Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
					while(iterator.hasNext()){
						Courier courier = iterator.next();
						if(courier!=null){
							if(courier!=null){
								order.setCourier(courier);
								orderRepository.save(order);
								System.err.println("(根据订单省市区匹配分区)自动分单成功......");
								generateWorkBill(order);
								System.out.println("(根据订单省市区匹配分区)工单生成成功，且短信通知...");
								return;
							}
						}
					}
				}
			}
		
		}
	}
	
	public void generateWorkBill(final Order order){
		
		WorkBill workBill = new WorkBill();
		workBill.setType("新");//工单类型
		workBill.setPickstate("待取件");//工单状态
		workBill.setRemark(order.getRemark());
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		final String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);//短信序号
		workBillService.save(workBill);
		
		jmsQueueTemplate.send("bos_sms", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				System.err.println("快递员手机号："+order.getCourier().getTelephone());
				mapMessage.setString("telephone",order.getCourier().getTelephone());
				mapMessage.setString("telcode", "短信序号："+smsNumber+",取件地址："+order.getSendAddress()
						+",联系电话："+order.getSendMobile()+",快递员捎话："+order.getSendMobileMsg());
//				mapMessage.setStringProperty("telcode", "短信序号："+smsNumber);
				return mapMessage;
			}
		});
		// 修改工单状态
		workBill.setPickstate("已短信通知");
	}

	@Override
	public Order findByOrderNum(String orderNum) {
		
		return orderRepository.findByOrderNum(orderNum);
	}
}
