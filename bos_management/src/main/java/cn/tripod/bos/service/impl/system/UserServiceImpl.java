package cn.tripod.bos.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.system.RoleRepository;
import cn.tripod.bos.dao.system.UserRepository;
import cn.tripod.bos.domain.system.Role;
import cn.tripod.bos.domain.system.User;
import cn.tripod.bos.service.system.UserService;
@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Override
	public User findByUsername(String username) {
		
		return userRepository.findByUsername(username);
	}
	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}
	@Override
	public void save(User user, String[] roleIds) {
		userRepository.save(user);
		if(roleIds!=null){
			for (String roleId : roleIds) {
				Role role = roleRepository.findOne(Integer.parseInt(roleId));
				user.getRoles().add(role);
			}
		}
	}

}
