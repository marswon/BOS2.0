package cn.tripod.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.tripod.bos.domain.system.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	@Query(value="from Role r inner join fetch r.users u where r.id = ?")
	List<Role> findByUser(Integer id);

}
