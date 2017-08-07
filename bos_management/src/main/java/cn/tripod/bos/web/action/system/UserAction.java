package cn.tripod.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.tripod.bos.domain.system.User;
import cn.tripod.bos.service.system.UserService;
import cn.tripod.bos.web.action.common.BaseAction;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User> {
	@Autowired
	private UserService userService;
	@Action(value="user_login",results={@Result(name="success",location="index.html",type="redirect"),
	@Result(name="input",location="login.html",type="redirect")})
	public String login(){
		
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		try {
			subject.login(token );//不报异常，登录成功
			ServletActionContext.getRequest().getSession().setAttribute("user", model);
			return SUCCESS;
		} catch (AuthenticationException e) {
			e.printStackTrace();//报异常，登录失败
			return INPUT;
		}
	}
	@Action(value="logout",results={@Result(name="success",location="login.html",type="redirect")})
	public String logout(){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();   //退出登录
		return SUCCESS;
	}
	@Action(value="user_queryPage",results={@Result(name="success",type="json")})
	public String queryPage(){
		List<User> users = userService.findAll();
		ActionContext.getContext().getValueStack().push(users);
		return SUCCESS;
	}
	private String[] roleIds;
	
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	@Action(value="user_save",results={@Result(name="success",location="pages/system/userlist.html",type="redirect")})
	public String save(){
		userService.save(model,roleIds);
		return SUCCESS;
	}
}
