package cn.tripod.bos.service.impl.transit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.transit.InOutStorageInfoRepository;
import cn.tripod.bos.dao.transit.TransitInfoRepository;
import cn.tripod.bos.domain.transit.InOutStorageInfo;
import cn.tripod.bos.domain.transit.TransitInfo;
import cn.tripod.bos.service.transit.InOutStorageInfoService;
@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {
	@Autowired
	private InOutStorageInfoRepository inOutStorageInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Override
	public void save(String inOutStoreId, InOutStorageInfo inOutStorageInfo) {
		//保存入库信息
		inOutStorageInfoRepository.save(inOutStorageInfo);
		//根据id获取持久态运输配送对象
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(inOutStoreId));
		//关联入库信息到运输配送对象
		transitInfo.getInOutStorageInfos().add(inOutStorageInfo);
		//修改运送信息状态、更新运送路径
		if("到达网点".equals(inOutStorageInfo.getOperation())){
			transitInfo.setStatus("到达网点");
			transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
		}
	}
}
