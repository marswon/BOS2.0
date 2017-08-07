package cn.tripod.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.base.StandardDao;
import cn.tripod.bos.dao.base.StandardRepository;
import cn.tripod.bos.domain.base.Standard;
import cn.tripod.bos.service.base.StandardService;
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	@Autowired
	private StandardDao standardDao;
	@Autowired
	private StandardRepository standardRepository;
	@Override
	public void save(Standard standard) {
		standardDao.save(standard);
	}
	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		return standardDao.findAll(pageable);
				
	}
	@Override
	public List<Standard> findAll() {
		return standardDao.findAll();
	}
	
}
