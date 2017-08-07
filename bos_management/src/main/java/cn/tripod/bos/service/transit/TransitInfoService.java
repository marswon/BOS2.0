package cn.tripod.bos.service.transit;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tripod.bos.domain.transit.TransitInfo;

public interface TransitInfoService {

	void creat(TransitInfo model, String idsStr);

	Page<TransitInfo> find(Pageable pageable);


}
