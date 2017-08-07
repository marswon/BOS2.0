package cn.tripod.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.tripod.bos.domain.base.FixedArea;
import cn.tripod.bos.service.base.FixedAreaService;
import cn.tripod.bos.web.action.common.BaseAction;
import cn.tripod.crm.domain.Customer;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea> {
	@Autowired
	private FixedAreaService fixedAreaService;
	@Action(value="fixedAreaSave",results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
	//保存
	public String fixedAreaSave(){
		fixedAreaService.save(model);
		return SUCCESS;
	}

	//条件查询
	@Action(value="fixedArea_QueryPage",results={@Result(name="success",type="json")})
	public String queryPage(){
		Pageable pageable = new PageRequest(page-1, rows);
		Specification<FixedArea> specification =new Specification<FixedArea>() {
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				//定区编码
				if(StringUtils.isNotBlank(model.getId())){
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					list.add(p1);
				}
				//所属单位
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), model.getCompany());
					list.add(p2);
				}
				//分区
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<FixedArea> pageData = fixedAreaService.findPageData(specification,pageable);
		pushPageDataToValueStack(pageData);	
		return SUCCESS;
	}
	//查询没有关联客户
	@Action(value="fexArea_findNoAssociationCustomers",results={@Result(name="success",type="json")})
	public String findNoAssociationCustomers(){
		Collection<? extends Customer> collection = WebClient
		.create("http://localhost:9002/crm_management/services/"
				+ "customerService/NoAssociationCustomers")   //  url
		.accept(MediaType.APPLICATION_JSON)    //返回参数类型
		.getCollection(Customer.class);    	//返回参数的数据类型
		
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	//查询已关联客户
	@Action(value="fexArea_findYesAssociationCustomers",results={@Result(name="success",type="json")})
	public String findYesAssociationCustomers(){
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9002/crm_management/services/customerService/YesAssociationCustomers/"+model.getId())
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	//属性驱动，获取被关联客户id
	private String[] customerIds;
					 
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	//将客户关联到定区
	@Action(value="fixArea_associationCustomerstoFixArea",
			results={@Result(name="success",type="redirect",location = "./pages/base/fixed_area.html")})
	public String associationCustomerstoFixArea(){
		System.out.println("bbbbbbb..............."+model.getId());
		String customerIdStr = StringUtils.join(customerIds, ",");
		WebClient.create("http://localhost:9002/crm_management/services/"
			+ "customerService/associationCustomerToFixArea?customerIdStr="
			+customerIdStr+"&fixedareaid="+model.getId()).put(null);
		return SUCCESS;
	}
	//属性驱动  获取快递员courierId、收派时间takeTimeId
	private Integer courierId;
	private Integer takeTimeId;
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	//关联快递员到定区
	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(name="success",type="redirect",location = "./pages/base/fixed_area.html")})
	public String associationCourierToFixedArea(){
		System.out.println("111111......"+model.getId());
		System.out.println("222222......"+courierId);
		System.out.println("333333......"+takeTimeId);
		fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
		return SUCCESS;
	}
}
