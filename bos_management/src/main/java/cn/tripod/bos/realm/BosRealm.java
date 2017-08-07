package cn.tripod.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tripod.bos.domain.system.Permission;
import cn.tripod.bos.domain.system.Role;
import cn.tripod.bos.domain.system.User;
import cn.tripod.bos.service.system.PermissionService;
import cn.tripod.bos.service.system.RoleService;
import cn.tripod.bos.service.system.UserService;
//@Service("bosRealm")   //在配置文件已手动配置，无需注解  手动配置可以添加相关参数
public class BosRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		// 授权认证
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		List<Role> roles = roleService.findByUser(user);
		for (Role role : roles) {
			authorizationInfo.addRole(role.getKeyword());
		}
		
		List<Permission> permissions = permissionService.findByUser(user);
		for (Permission permission : permissions) {
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		System.err.println("权限认证...");
		return authorizationInfo;   //将授权结果返回给安全管理器SecurityManager
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		System.err.println("登录认证...");
		// 登录认证
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		if(user!=null){
			//将登陆信息传递给授权对象
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		}else{
			return null;
		}
	}

}
