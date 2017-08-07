package cn.tripod.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.tripod.bos.domain.system.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer>,
		JpaSpecificationExecutor<Menu> {
	@Query(value="from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id = ?")
	public List<Menu> findByUser(Integer id);

}
