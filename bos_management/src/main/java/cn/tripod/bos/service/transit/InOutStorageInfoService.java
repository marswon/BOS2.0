package cn.tripod.bos.service.transit;

import cn.tripod.bos.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoService {

	void save(String inOutStoreId, InOutStorageInfo model);

}
