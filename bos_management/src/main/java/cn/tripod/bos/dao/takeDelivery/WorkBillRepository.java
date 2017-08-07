package cn.tripod.bos.dao.takeDelivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.tripod.bos.domain.takeDelivery.WorkBill;

public interface WorkBillRepository extends JpaRepository<WorkBill, Integer>,
		JpaSpecificationExecutor<WorkBill> {

}
