package cn.tripod.bos.web.action;


import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.base.Area;
import cn.tripod.bos.domain.takeDelivery.Constant;
import cn.tripod.bos.domain.takeDelivery.Order;
import cn.tripod.crm.domain.Customer;
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {
	
	private String sendAreaInfo;
	private String recAreaInfo;
	
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	@Action(value="order_save",results={@Result(name="success",location="index.html",type="redirect"),
			@Result(name="input",location="error.json",type="redirect")})
	public String saveOrder(){
		String[] send = sendAreaInfo.split("/");
		String[] rec = recAreaInfo.split("/");
		Area sendArea = new Area();
		sendArea.setProvince(send[0]);
		sendArea.setCity(send[1]);
		sendArea.setDistrict(send[2]);
		
		Area recArea = new Area();
		recArea.setProvince(rec[0]);
		recArea.setCity(rec[1]);
		recArea.setDistrict(rec[2]);
		
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		
		Customer customer = (Customer)ServletActionContext.getRequest().getSession().getAttribute("customer");
		System.out.println("customer........."+customer);
		model.setCustomer_id(customer.getId());
		System.err.println(model.getSendCompany());
		WebClient.create(Constant.BOS_MANAGEMENT_URL+"/services/orderService/order")
		.type(MediaType.APPLICATION_JSON)
		.post(model);
		System.err.println(model.getSendCompany());
		return SUCCESS;
	}
}
