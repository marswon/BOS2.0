package cn.tripod.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tripod.bos.domain.base.Courier;

public interface CourierDao extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier> {
	@Query(value="update Courier set deltag = 1 where id=?",nativeQuery=false)
	@Modifying
	public void updateDeltag(int parseInt);
	@Query(value="update Courier set deltag = null where id=?",nativeQuery=false)
	@Modifying
	public void restoreDeltag(int parseInt);
	
}
