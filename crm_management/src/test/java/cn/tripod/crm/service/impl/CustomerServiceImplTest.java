package cn.tripod.crm.service.impl;

/*import static org.junit.Assert.*;
*/
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.tripod.crm.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerServiceImplTest {
	@Autowired
	private CustomerService customerService;
	@Test
	public void testFindNoAssociationCustomers() {
		System.out.println(customerService.findNoAssociationCustomers());
	}

	@Test
	public void testFindYesAssociationCustomers() {
		System.out.println(customerService.findYesAssociationCustomers("11"));
	}

	@Test
	public void testAssociationCustomerToFixArea() {
	}

}
