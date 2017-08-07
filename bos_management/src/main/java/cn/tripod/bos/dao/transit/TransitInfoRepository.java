package cn.tripod.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.tripod.bos.domain.transit.TransitInfo;

public interface TransitInfoRepository extends JpaRepository<TransitInfo, Integer> {

}
