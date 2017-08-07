package cn.tripod.bos.service.impl.transit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.takeDelivery.WayBillRepository;
import cn.tripod.bos.dao.transit.TransitInfoRepository;
import cn.tripod.bos.domain.takeDelivery.WayBill;
import cn.tripod.bos.domain.transit.TransitInfo;
import cn.tripod.bos.service.transit.TransitInfoService;
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Autowired
	private WayBillRepository wayBillRepository;
	@Override
	public void creat(TransitInfo model, String idsStr) {
		if(StringUtils.isNotBlank(idsStr)){
			for (String id : idsStr.split(",")) {
				WayBill persistWayBill = wayBillRepository.findOne(Integer.parseInt(id));
				if(persistWayBill.getSignStatus()==1){
					//生成TransitInfo的信息
					TransitInfo transitInfo = new TransitInfo();
					transitInfo.setWayBill(persistWayBill);
					transitInfo.setStatus("出入库中转");
					transitInfoRepository.save(transitInfo);
					//修改运单的状态  因为persistWayBill为持久性对象，故已关联数据库，只需set赋值即可更新数据库对应栏位
					persistWayBill.setSignStatus(2);
				}
			}
		}

	}
	@Override
	public Page<TransitInfo> find(Pageable pageable) {
		
		return transitInfoRepository.findAll(pageable);
	}
	

}
