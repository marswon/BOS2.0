package cn.tripod.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tripod.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>,JpaSpecificationExecutor<Customer> {

	public List<Customer> findByFixedAreaIdIsNull();
						
	public List<Customer> findByFixedAreaId(String fixAeraId);
	
	@Query("update Customer set fixedAreaId = ? where id = ? ")
	@Modifying
	public void updateFixAreaId(String fixAreaId, Integer parseInt);
	
	@Query("update Customer set fixedAreaId = null where fixedAreaId = ? ")
	@Modifying
	public void clearFixedAreaId(String fixAreaId);

	public Customer findByTelephone(String telephone);
	
	@Query(value="update Customer set type = 1 where telephone = ?")
	@Modifying
	public void updateType(String telephone);

	public Customer findByTelephoneAndPassword(String telephone, String password);
	
	@Query(value="select fixedAreaId from Customer where address = ?")
	public String findfixedAreaIdByAddress(String sendAddress);

}
