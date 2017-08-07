package cn.tripod.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.transit.InOutStorageInfo;
import cn.tripod.bos.service.transit.InOutStorageInfoService;
import cn.tripod.bos.web.action.common.BaseAction;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
@SuppressWarnings("serial")
public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo> {
	@Autowired
	private InOutStorageInfoService inOutStorageInfoService;
	
	private String inOutStoreId;
	
	public void setInOutStoreId(String inOutStoreId) {
		this.inOutStoreId = inOutStoreId;
	}

	@Action(value="inoutStoreForm_save",results={@Result(name="success",location="pages/transit/transitinfo.html",type="redirect")})
	public String save(){
		inOutStorageInfoService.save(inOutStoreId,model);
		return SUCCESS;
	}
}
