package cn.tripod.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.tripod.bos.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoRepository extends JpaRepository<InOutStorageInfo, Integer> {

}
