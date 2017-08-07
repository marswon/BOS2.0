package cn.tripod.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.base.Standard;
import cn.tripod.bos.service.base.StandardService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{
	private Standard standard = new Standard();
	@Autowired
	private StandardService standardService;
	@Override
	public Standard getModel() {
		return standard;
	}
	//添加
	@Action(value="saveStandard",results={@Result(name="success",location="/pages/base/standard.html",type="redirect")})
	public String saveStandard(){
		standardService.save(standard);
		return SUCCESS;
	}
	//属性驱动获取分页参数
	private Integer page;
	private Integer rows;
	
	public void setPage(Integer page) {
		this.page = page;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	//分页显示
	@Action(value="standard_pageQuery",results={@Result(name="success",type="json")})
	public String standard_pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Standard> pageData = standardService.findPageData(pageable);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pageData.getNumberOfElements());
		map.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(map);
		System.out.println(map);
		return SUCCESS;
	}
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String standard_findAll(){
		List<Standard> list = standardService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
