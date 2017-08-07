package cn.tripod.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.tripod.bos.domain.base.FixedArea;

public interface FixedAreaDao extends JpaRepository<FixedArea, String>,
		JpaSpecificationExecutor<FixedArea> {

}
