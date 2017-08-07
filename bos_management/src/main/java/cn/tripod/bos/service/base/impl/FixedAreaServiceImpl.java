package cn.tripod.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.base.CourierDao;
import cn.tripod.bos.dao.base.FixedAreaDao;
import cn.tripod.bos.dao.base.TakeTimeRepository;
import cn.tripod.bos.domain.base.Courier;
import cn.tripod.bos.domain.base.FixedArea;
import cn.tripod.bos.domain.base.TakeTime;
import cn.tripod.bos.service.base.FixedAreaService;
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	@Autowired
	private FixedAreaDao fixedAreaDao;
	@Override
	public void save(FixedArea model) {
		fixedAreaDao.save(model);
	}
	@Override
	public Page<FixedArea> findPageData(Specification<FixedArea> specification,
			Pageable pageable) {
		return fixedAreaDao.findAll(specification, pageable);
	}
	@Autowired
	private CourierDao courierDao;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	@Override
	public void associationCourierToFixedArea(FixedArea model,
			Integer courierId, Integer takeTimeId) {
		FixedArea fixedArea = fixedAreaDao.findOne(model.getId());
		Courier courier = courierDao.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		//将快递员关联到定区
		fixedArea.getCouriers().add(courier);
		//将收派时间关联快递员
		courier.setTakeTime(takeTime);
	}

}
