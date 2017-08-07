package cn.tripod.bos.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.system.MenuRepository;
import cn.tripod.bos.domain.system.Menu;
import cn.tripod.bos.domain.system.User;
import cn.tripod.bos.service.system.MenuService;
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public List<Menu> findAll() {
		
		return menuRepository.findAll();
	}
	
	@Override
	public void save(Menu menu) {
		menuRepository.save(menu);
	}

	@Override
	public List<Menu> findByUser(User user) {
		if("admin".equals(user.getUsername())){
			return menuRepository.findAll();
		}else{
			
			return menuRepository.findByUser(user.getId());
		}
	}
}
