package cn.tripod.bos.web.action.takeDelivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.takeDelivery.WayBill;
import cn.tripod.bos.service.takeDelivery.WayBillService;
import cn.tripod.bos.web.action.common.BaseAction;
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class WayBillAction extends BaseAction<WayBill> {
	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);
	@Autowired
	private WayBillService wayBillService;
	//快速录入运单
	@Action(value="waybill_save",results={@Result(name="success",type="json")})
	public String save(){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(model.getOrder()!=null&&(model.getOrder().getId()==null
					||model.getOrder().getId()==0)){
				model.setOrder(null);
			}
			wayBillService.save(model);
			result.put("success", true);
			result.put("msg", "保存运单成功...");
			LOGGER.info("保存运单成功，运单号："+model.getWayBillNum());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success",false);
			result.put("msg", "保存运单失败...");
			LOGGER.error("保存运单失败,运单号："+model.getWayBillNum());
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	//显示快速录入运单
	@Action(value="waybill_queryPage",results={@Result(name="success",type="json")})
	public String queryPage(){
		Pageable pageable = new PageRequest(page-1, rows, new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
		Page<WayBill> pageData = wayBillService.findAll(model,pageable);
		Map<String , Object> result = new HashMap<String, Object>();
		result.put("total", pageData.getTotalElements());  //总记录条数
		result.put("rows", pageData.getContent());    //每条记录的内容
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	//查询指定运单号记录
	@Action(value="findByWayBillNum",results={@Result(name="success",type="json")})
	public String findByWayBillNum(){
		WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
		Map<String, Object> result = new HashMap<String, Object>();
		if(wayBill==null){
			result.put("success", false);
		}else{
			result.put("success", true);
			result.put("wayBillData", wayBill);
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
}
