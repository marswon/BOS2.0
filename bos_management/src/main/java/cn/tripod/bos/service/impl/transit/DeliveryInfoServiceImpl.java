package cn.tripod.bos.service.impl.transit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.transit.DeliveryInfoRepository;
import cn.tripod.bos.dao.transit.TransitInfoRepository;
import cn.tripod.bos.domain.transit.DeliveryInfo;
import cn.tripod.bos.domain.transit.TransitInfo;
import cn.tripod.bos.service.transit.DeliveryInfoService;
@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {
	@Autowired
	private DeliveryInfoRepository deliveryInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Override
	public void save(String deliveryId, DeliveryInfo deliveryInfo) {
		//保存配送信息
		deliveryInfoRepository.save(deliveryInfo);
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(deliveryId));
		//配送关联运送
		transitInfo.setDeliveryInfo(deliveryInfo);
		//修改运送状态
		transitInfo.setStatus("开始配送");
	}

}
