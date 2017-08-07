package cn.tripod.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.tripod.bos.domain.system.Role;
import cn.tripod.bos.service.system.RoleService;
import cn.tripod.bos.web.action.common.BaseAction;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class RoleAction extends BaseAction<Role> {
	@Autowired
	private RoleService roleService;
	@Action(value="role_queryPage",results={@Result(name="success",type="json")})
	public String queryPage(){
		List<Role> roles = roleService.findAll();
		ActionContext.getContext().getValueStack().push(roles);
		return SUCCESS;
	}
	private String[] permissionIds;
	private String menuIds;

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	
	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	@Action(value="role_save",results={@Result(name="success",location="pages/system/role.html",type="redirect")})
	public String save(){
		roleService.save(model,permissionIds,menuIds);
		return SUCCESS;
	}
}
