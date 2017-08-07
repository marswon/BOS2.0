package cn.tripod.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.tripod.bos.domain.base.TakeTime;
import cn.tripod.bos.service.base.TakeTimeService;
import cn.tripod.bos.web.action.common.BaseAction;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TakeTimeAction extends BaseAction<TakeTime> {
	@Autowired
	private TakeTimeService takeTimeService;
	//查询所有的收派时间
	@Action(value="takeTime_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<TakeTime> list = takeTimeService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
