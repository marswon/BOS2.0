package cn.tripod.bos.dao.takeDelivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.tripod.bos.domain.takeDelivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>,
	JpaSpecificationExecutor<Integer>{

	public Order findByOrderNum(String orderNum);

}
