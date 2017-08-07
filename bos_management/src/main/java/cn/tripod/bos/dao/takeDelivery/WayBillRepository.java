package cn.tripod.bos.dao.takeDelivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.tripod.bos.domain.takeDelivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill, Integer> ,
	JpaSpecificationExecutor<WayBill>
{

	public WayBill findByWayBillNum(String wayBillNum);
	
}
