package cn.tripod.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.tripod.bos.domain.system.Menu;
import cn.tripod.bos.domain.system.User;
import cn.tripod.bos.service.system.MenuService;
import cn.tripod.bos.web.action.common.BaseAction;
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu> {
	@Autowired
	private MenuService menuService;
	@Action(value="menu_queryPage",results={@Result(name="success",type="json")})
	public String queryPage(){
		List<Menu> menus = menuService.findAll();
		ActionContext.getContext().getValueStack().push(menus);
		return SUCCESS;
	}
	@Action(value="menu_save",results={@Result(name="success",location="pages/system/menu.html",type="redirect")})
	public String save(){
		if(model.getParentMenu()!=null&&model.getParentMenu().getId()==0){
			model.setParentMenu(null);
		}else{
			menuService.save(model);
		}
		
		return SUCCESS;
	}
	
	@Action(value="menu_showmenu",results={@Result(name="success",type="json")})
	public String showmenu(){
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		List<Menu> menus = menuService.findByUser(user);
		ActionContext.getContext().getValueStack().push(menus);
		return SUCCESS;
	}
}
