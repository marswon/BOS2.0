package cn.tripod.bos.web.action.transit;

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







import com.opensymphony.xwork2.ActionContext;

import cn.tripod.bos.domain.transit.TransitInfo;
import cn.tripod.bos.service.transit.TransitInfoService;
import cn.tripod.bos.web.action.common.BaseAction;

@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TransitInfoAction extends BaseAction<TransitInfo> {
	
	@Autowired
	private TransitInfoService transitInfoService;
	
	private String idsStr;
	
	public void setIdsStr(String idsStr) {
		this.idsStr = idsStr;
	}

	@Action(value="creatTransit",results={@Result(name="success",type="json")})
	public String creatTransit(){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			transitInfoService.creat(model,idsStr);
			result.put("success", true);
			result.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败");
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	@Action(value="transit_queryPage",results={@Result(name="success",type="json")})
	public String queryPage(){
		Pageable pageable = new PageRequest(page-1, rows);
		Page<TransitInfo> pageData = transitInfoService.find(pageable);
		System.err.println(pageData.getContent());
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
}
