package cn.tripod.bos.service.takeDelivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tripod.bos.domain.takeDelivery.WayBill;

public interface WayBillService {

	void save(WayBill model);

	Page<WayBill> findAll(WayBill wayBill, Pageable pageable);

	WayBill findByWayBillNum(String wayBillNum);

	List<WayBill> findAll(WayBill model);

}
