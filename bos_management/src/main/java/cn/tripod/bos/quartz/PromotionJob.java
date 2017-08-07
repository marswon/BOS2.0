package cn.tripod.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.tripod.bos.service.takeDelivery.PromotionService;

public class PromotionJob implements Job{
	@Autowired
	private PromotionService promotionService;
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		promotionService.updateType(new Date());
		
	}
	
}
