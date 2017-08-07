package cn.tripod.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tripod.crm.dao.CustomerRepository;
import cn.tripod.crm.domain.Customer;
import cn.tripod.crm.service.CustomerService;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	@Override
	public List<Customer> findNoAssociationCustomers() {
		return customerRepository.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findYesAssociationCustomers(String fixAreaId) {
		return customerRepository.findByFixedAreaId(fixAreaId);
	}

	@Override
	public void associationCustomerToFixArea(String customerIdStr, String fixAreaId) {
		// 解除关联动作
		customerRepository.clearFixedAreaId(fixAreaId);
		//System.out.println(customerIdStr);
		// 切割字符串 1,2,3
		if (StringUtils.isBlank(customerIdStr)||"null".equals(customerIdStr)) {
			return;
		}
		String[] ids = customerIdStr.split(",");
		for (String id : ids) {
			customerRepository.updateFixAreaId(fixAreaId,Integer.parseInt(id));
		}
	}

	@Override
	public void register(Customer model) {
		customerRepository.save(model);
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return customerRepository.findByTelephone(telephone);
	}

	@Override
	public void updateType(String telephone) {
		customerRepository.updateType(telephone);
	}

	@Override
	public Customer login(String telephone, String password) {
		System.err.println(customerRepository.findByTelephoneAndPassword(telephone,password));
		return customerRepository.findByTelephoneAndPassword(telephone,password);
	}

	@Override
	public String findfixedAreaIdByAddress(String sendAddress) {
		
		return customerRepository.findfixedAreaIdByAddress(sendAddress);
	}

}
