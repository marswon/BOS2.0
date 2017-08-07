package cn.tripod.bos.service.system;

import java.util.List;

import cn.tripod.bos.domain.system.Permission;
import cn.tripod.bos.domain.system.User;

public interface PermissionService {

	List<Permission> findByUser(User user);

	List<Permission> findAll();

	void save(Permission model);

}
