package cn.tripod.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.system.PermissionRepository;
import cn.tripod.bos.domain.system.Permission;
import cn.tripod.bos.domain.system.User;
import cn.tripod.bos.service.system.PermissionService;
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionRepository permissionRepository;
	@Override
	public List<Permission> findByUser(User user) {
		
		return permissionRepository.findByUser(user.getId());
	}
	@Override
	public List<Permission> findAll() {
		
		return permissionRepository.findAll();
	}
	@Override
	public void save(Permission permission) {
		
		permissionRepository.save(permission);
	}

}
