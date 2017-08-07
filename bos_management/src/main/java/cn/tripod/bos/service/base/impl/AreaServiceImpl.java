package cn.tripod.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.base.AreaDao;
import cn.tripod.bos.domain.base.Area;
import cn.tripod.bos.service.base.AreaService;
@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaDao areaDao;
	@Override
	public void save(List<Area> areaList) {
		areaDao.save(areaList);
	}
	@Override
	public Page<Area> findPageData(Specification<Area> specification,
			Pageable pageable) {
		return areaDao.findAll(specification, pageable);
	}

}
