package cn.tripod.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.tripod.bos.domain.base.TakeTime;

public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer> {

}
