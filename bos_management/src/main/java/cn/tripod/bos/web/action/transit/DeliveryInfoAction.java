package cn.tripod.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.transit.DeliveryInfo;
import cn.tripod.bos.service.transit.DeliveryInfoService;
import cn.tripod.bos.web.action.common.BaseAction;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class DeliveryInfoAction extends BaseAction<DeliveryInfo> {
	@Autowired
	private DeliveryInfoService deliveryInfoService;
	
	private String deliveryId;
	
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	@Action(value="delivery_save",results={@Result(name="success",location="pages/transit/transitinfo.html",type="redirect")})
	public String save(){
		deliveryInfoService.save(deliveryId,model);
		return SUCCESS;
	}
}
