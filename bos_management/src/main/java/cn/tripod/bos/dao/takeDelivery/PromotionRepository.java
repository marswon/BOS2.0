package cn.tripod.bos.dao.takeDelivery;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tripod.bos.domain.takeDelivery.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>,
		JpaSpecificationExecutor<Integer> {
	@Query(value="update Promotion set status = '2' where endDate < ? and status = '1'")
	@Modifying
	public void updateType(Date date);

}
