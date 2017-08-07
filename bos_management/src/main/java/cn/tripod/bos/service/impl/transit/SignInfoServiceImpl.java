package cn.tripod.bos.service.impl.transit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.transit.SignInfoRepository;
import cn.tripod.bos.dao.transit.TransitInfoRepository;
import cn.tripod.bos.domain.transit.SignInfo;
import cn.tripod.bos.domain.transit.TransitInfo;
import cn.tripod.bos.indexdao.WayBillIndexRepository;
import cn.tripod.bos.service.transit.SignInfoService;
@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {
	@Autowired
	private SignInfoRepository signInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	@Override
	public void save(String signInfoId, SignInfo signInfo) {
		//保存签收信息
		signInfoRepository.save(signInfo);
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(signInfoId));
		if("正常".equals(signInfo.getSignType())){
			transitInfo.setStatus("正常签收");
			transitInfo.getWayBill().setSignStatus(3);
			//同步索引
			wayBillIndexRepository.save(transitInfo.getWayBill());
		}else{
			transitInfo.setStatus("异常");
			transitInfo.getWayBill().setSignStatus(4);
			//同步索引
			wayBillIndexRepository.save(transitInfo.getWayBill());
		}
		
	}

}
