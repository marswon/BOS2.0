package cn.tripod.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.base.Courier;
import cn.tripod.bos.service.base.CourierService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
	private Courier courier = new Courier();
	@Autowired
	private CourierService courierService;
	@Override
	public Courier getModel() {
		return courier;
	}
	//保存
	@Action(value="saveCourier",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String saveCourier(){
		courierService.save(courier);
		return SUCCESS;
	}
	//获取分页参数
	private Integer page;
	private Integer rows;
	
	public void setPage(Integer page) {
		this.page = page;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	//查询
	@Action(value="courier_pageQuery",results={@Result(name="success",type="json")})
	public String courier_pageQuery(){
		//PageRequest中的page是从0开始查询
		Pageable pageable = new PageRequest(page-1, rows);
		//条件查询
		Specification<Courier> specification = new Specification<Courier>() {
			
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				//单表查询
				//工号
				if(StringUtils.isNotBlank(courier.getCourierNum())){
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(p1);
				}
				//单位
				if(StringUtils.isNotBlank(courier.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), courier.getCompany());
					list.add(p2);
				}
				//类型
				if(StringUtils.isNotBlank(courier.getType())){
					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				//多表查询    需要向root中加入standard属性
				Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);
				if(courier.getStandard()!=null && StringUtils.isNotBlank(courier.getStandard().getName())){
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%"+courier.getStandard().getName()+"%");
					list.add(p4);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<Courier> pageData = courierService.findPageData(specification,pageable);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total",pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	@Action(value="delBatch",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String delBatch(){
//		String ids = (String) ServletActionContext.getRequest().getAttribute("ids");
		String[] ids_Arr = ids.split(",");
		courierService.delBatch(ids_Arr);
		return SUCCESS;
	}
	@Action(value="restoreDeltag",results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String restoreDeltag(){
		String[] ids_Arr = ids.split(",");
		courierService.restoreDeltag(ids_Arr);
		return SUCCESS;
	}
	@Action(value="courier_findnoassociation",results={@Result(name="success",type="json")})
	public String findnoassociation(){
		List<Courier> list = courierService.findnoassociation();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
