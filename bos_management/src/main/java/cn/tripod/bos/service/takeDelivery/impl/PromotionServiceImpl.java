package cn.tripod.bos.service.takeDelivery.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.takeDelivery.PromotionRepository;
import cn.tripod.bos.domain.takeDelivery.PageBean;
import cn.tripod.bos.domain.takeDelivery.Promotion;
import cn.tripod.bos.service.takeDelivery.PromotionService;
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
	@Autowired
	private PromotionRepository promotionRepository;
	
	@Override
	public void save(Promotion model) {
		promotionRepository.save(model);
	}

	@Override
	public PageBean<Promotion> findPage(int page, int rows) {
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setTotalcount(pageData.getTotalPages());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}

	@Override
	public Promotion findPromotion(Integer id) {
		System.out.println(promotionRepository.findOne(id));
		return promotionRepository.findOne(id);
	}

	@Override
	public void updateType(Date date) {
		promotionRepository.updateType(date);
		
	}


}
