package cn.tripod.bos.service.impl.system;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.system.MenuRepository;
import cn.tripod.bos.dao.system.PermissionRepository;
import cn.tripod.bos.dao.system.RoleRepository;
import cn.tripod.bos.domain.system.Menu;
import cn.tripod.bos.domain.system.Permission;
import cn.tripod.bos.domain.system.Role;
import cn.tripod.bos.domain.system.User;
import cn.tripod.bos.service.system.RoleService;
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Override
	public List<Role> findByUser(User user) {
		if(user.getUsername().equals("admin")){
			return roleRepository.findAll();
		}else{
			return roleRepository.findByUser(user.getId());
		}
	}
	@Override
	public List<Role> findAll() {
		
		return roleRepository.findAll();
	}
	@Override
	public void save(Role role, String[] permissionIds, String menuIds) {
		roleRepository.save(role);
		//关联权限
		if(permissionIds!=null){
			for (String permissionId : permissionIds) {
				Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
				role.getPermissions().add(permission);
			}
		}
		//关联菜单
		if (StringUtils.isNoneBlank(menuIds)) {
			String[] menuIds_arr = menuIds.split(",");
			for (String menuId : menuIds_arr) {
				Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
				role.getMenus().add(menu);
			}
		}
		
	}

}
