package cn.tripod.bos.service.system;

import java.util.List;

import cn.tripod.bos.domain.system.User;

public interface UserService {

	User findByUsername(String username);

	List<User> findAll();

	void save(User model, String[] roleIds);

}
