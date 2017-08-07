package cn.tripod.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.transit.SignInfo;
import cn.tripod.bos.service.transit.SignInfoService;
import cn.tripod.bos.web.action.common.BaseAction;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class SignInfoAction extends BaseAction<SignInfo> {
	
	@Autowired
	private SignInfoService signInfoService;
	
	private String signInfoId;
	
	public void setSignInfoId(String signInfoId) {
		this.signInfoId = signInfoId;
	}

	@Action(value="sign_save",results={@Result(name="success",location="pages/transit/transitinfo.html",type="redirect")})
	public String save(){
		signInfoService.save(signInfoId,model);
		return SUCCESS;
	}
}
