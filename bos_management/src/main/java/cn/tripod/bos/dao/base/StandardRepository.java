package cn.tripod.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.tripod.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer>  {
	public List<Standard> findByName(String name);
	@Query(value="from Standard where name=?",nativeQuery=false)
	public List<Standard> queryName(String name);
}
