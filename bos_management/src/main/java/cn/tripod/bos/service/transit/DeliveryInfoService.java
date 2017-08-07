package cn.tripod.bos.service.transit;

import cn.tripod.bos.domain.transit.DeliveryInfo;

public interface DeliveryInfoService {

	void save(String deliveryId, DeliveryInfo model);

}
