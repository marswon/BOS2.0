package cn.tripod.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.tripod.crm.domain.Customer;

public interface CustomerService {
	//查询所有未关联定区客户列表
	@Path("/NoAssociationCustomers")   //服务访问路径
	@GET	//操作方式  查询
	@Produces({"application/xml","application/json"})   //方法返回值的数据格式
	public List<Customer> findNoAssociationCustomers();
	//查询已关联定区的客户列表
	@Path("/YesAssociationCustomers/{fixedareaid}")
	@GET
	@Produces({"application/xml","application/json"})
	public List<Customer> findYesAssociationCustomers(@PathParam("fixedareaid") String fixedareaid);
	//将客户关联到定区上，将客户id拼成字符串（格式：1,2,3）
	@Path("/associationCustomerToFixArea")
	@PUT														  									
	public void associationCustomerToFixArea(@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedareaid") String fixedareaid);
	
	@Path("/registerCustomer")
	@POST
	@Consumes({"application/xml","application/json"})  //传递参数的数据格式
	public void register(Customer model);
	
	@Path("/customer/telephone/{telephone}")
	@GET
	public Customer findByTelephone(@PathParam("telephone") String telephone);
	
	@Path("/customer/updateType/{telephone}")
	@PUT
	public void updateType(@PathParam("telephone") String telephone);
	
	@Path("/customer_login")
	@GET
	@Produces({"application/xml","application/json"})
	public Customer login(@QueryParam("telephone") String telephone,
			@QueryParam("password") String password);
	
	@Path("/findfixedAreaIdByAddress")
	@GET
	@Produces({"application/xml","application/json"})
	public String findfixedAreaIdByAddress(@QueryParam("sendAddress") String sendAddress);
	
}
