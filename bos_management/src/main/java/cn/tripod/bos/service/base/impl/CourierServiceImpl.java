package cn.tripod.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.bos.dao.base.CourierDao;
import cn.tripod.bos.domain.base.Courier;
import cn.tripod.bos.domain.base.FixedArea;
import cn.tripod.bos.service.base.CourierService;
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	@Autowired
	private CourierDao courierDao;
	
	@Override
	@RequiresPermissions("courier:add")
	public void save(Courier courier) {
		courierDao.save(courier);
	}

	@Override
	public Page<Courier> findPageData(Specification<Courier> specification,
			Pageable pageable) {
		return courierDao.findAll(specification, pageable);
	}

	@Override
	public void delBatch(String[] ids_Arr) {
		for (int i = 0; i < ids_Arr.length; i++) {
			courierDao.updateDeltag(Integer.parseInt(ids_Arr[i]));
		}
	}

	@Override
	public void restoreDeltag(String[] ids_Arr) {
		for (int i = 0; i < ids_Arr.length; i++) {
			courierDao.restoreDeltag(Integer.parseInt(ids_Arr[i]));
		}
	}
	//查询没有关联定区的快递员
	@Override
	public List<Courier> findnoassociation() {
		Specification<Courier> specification = new Specification<Courier>() {
			
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				//判断条件：Set<FixedArea> fixedAreas的size为空。
				Predicate p1 = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return p1;
			}
		};
		return courierDao.findAll(specification);
	}

}
