package cn.tripod.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.tripod.bos.domain.system.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);

}
