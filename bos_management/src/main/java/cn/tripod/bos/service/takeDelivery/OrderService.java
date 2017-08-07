package cn.tripod.bos.service.takeDelivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import cn.tripod.bos.domain.takeDelivery.Order;

public interface OrderService {
	
	@Path("/order")
	@POST
	@Consumes({"application/xml","application/json"})
	public void addOrder(Order model);

	public Order findByOrderNum(String orderNum);
}
