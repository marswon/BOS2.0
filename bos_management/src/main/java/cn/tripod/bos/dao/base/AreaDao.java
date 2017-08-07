package cn.tripod.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tripod.bos.domain.base.Area;
import cn.tripod.bos.domain.base.Courier;

public interface AreaDao extends JpaRepository<Area, String> ,JpaSpecificationExecutor<Area>{

	Area findByProvinceAndCityAndDistrict(String province, String city,
			String district);
	
}
