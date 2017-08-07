package cn.tripod.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.base.TakeTimeRepository;
import cn.tripod.bos.domain.base.TakeTime;
import cn.tripod.bos.service.base.TakeTimeService;
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

}
