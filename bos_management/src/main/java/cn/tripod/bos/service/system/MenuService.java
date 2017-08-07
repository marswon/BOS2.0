package cn.tripod.bos.service.system;

import java.util.List;

import cn.tripod.bos.domain.system.Menu;
import cn.tripod.bos.domain.system.User;

public interface MenuService {

	List<Menu> findAll();

	void save(Menu model);

	List<Menu> findByUser(User user);

}
