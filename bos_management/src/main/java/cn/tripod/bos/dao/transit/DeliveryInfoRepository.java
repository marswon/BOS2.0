package cn.tripod.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.tripod.bos.domain.transit.DeliveryInfo;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer> {

}
