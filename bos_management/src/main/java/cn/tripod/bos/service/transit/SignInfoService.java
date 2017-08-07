package cn.tripod.bos.service.transit;

import cn.tripod.bos.domain.transit.SignInfo;

public interface SignInfoService {

	void save(String signInfoId, SignInfo model);

}
