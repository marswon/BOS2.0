package cn.tripod.bos.service.takeDelivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.takeDelivery.WorkBillRepository;
import cn.tripod.bos.domain.takeDelivery.WorkBill;
import cn.tripod.bos.service.takeDelivery.WorkBillService;
@Service
@Transactional
public class WorkBillServiceImpl implements WorkBillService {
	@Autowired
	private WorkBillRepository workBillRepository;
	
	@Override
	public void save(WorkBill workBill) {
		
		workBillRepository.save(workBill);
	}

}
