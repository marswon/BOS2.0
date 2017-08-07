package cn.tripod.bos.web.action.takeDelivery;


import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.takeDelivery.Order;
import cn.tripod.bos.service.takeDelivery.OrderService;
import cn.tripod.bos.web.action.common.BaseAction;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {
	@Autowired
	private OrderService orderService;
	
	@Action(value="findByOrderNum",results={@Result(name="success",type="json")})
	public String findByOrderNum(){
		Order order = orderService.findByOrderNum(model.getOrderNum());
		Map< String, Object> result = new HashMap<String, Object>();
		if(order==null){
			result.put("success", false);
		}else{
			result.put("success", true);
			result.put("orderData", order);
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
