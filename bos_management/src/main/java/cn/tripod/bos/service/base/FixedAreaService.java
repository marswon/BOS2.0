package cn.tripod.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.tripod.bos.domain.base.FixedArea;

public interface FixedAreaService {

	void save(FixedArea model);

	Page<FixedArea> findPageData(Specification<FixedArea> specification,
			Pageable pageable);

	void associationCourierToFixedArea(FixedArea model, Integer courierId,
			Integer takeTimeId);

}
