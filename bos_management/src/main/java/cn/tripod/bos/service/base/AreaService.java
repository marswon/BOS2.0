package cn.tripod.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.tripod.bos.domain.base.Area;

public interface AreaService {

	void save(List<Area> areaList);

	Page<Area> findPageData(Specification<Area> specification, Pageable pageable);

}
