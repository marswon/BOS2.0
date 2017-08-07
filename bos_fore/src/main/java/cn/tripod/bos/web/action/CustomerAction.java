package cn.tripod.bos.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;



import cn.tripod.bos.domain.takeDelivery.Constant;
import cn.tripod.bos.utils.MailUtils;
import cn.tripod.crm.domain.Customer;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CustomerAction extends BaseAction<Customer> {
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsQueueTemplate;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	//发送短信验证码
	@Action(value="customer_sendSms")
	public String sendSms(){
		//生成验证码
		final String telcode = RandomStringUtils.randomNumeric(4);
		//将code存入session中，以便校正
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), telcode);
		try {
			//调用MQ，发送一条消息
			jmsQueueTemplate.send("bos_sms", new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					//获取消息对象
					MapMessage mapMessage = session.createMapMessage();
					//向消息中添加内容
					mapMessage.setString("telephone", model.getTelephone());
					mapMessage.setString("telcode", telcode);
					return mapMessage;
				}
			});
			System.out.println(telcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	//属性驱动   获取表单验证码
	private String checkcode;
	private String repassword;
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	
	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
	
	//注册
	@Action(value="customer_register",results={@Result(name="success",location="signup-success.html",type="redirect"),
			@Result(name="input",location="signup.html",type="redirect")})
	public String register(){
		Map<String, String> error_map = new HashMap<String, String>();//用来存取见证错误的信息
		String code_session = (String)ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
		
		//手机号是否被注册
		Customer customer = WebClient.create("http://localhost:9002/crm_management/"
				+ "services/customerService/customer/telephone/"+model.getTelephone())
		.accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if(customer!=null){
			error_map.put("tel","该手机号已经注册");
		}
		//验证码是否一样
		if (checkcode == null || !code_session.equals(checkcode)) {
			error_map.put("code", "验证码错误");
		}
		//密码是否一样
		if(!model.getPassword().equals(repassword)){
			error_map.put("password", "密码不一致");
		}
		//判断校正是否OK
		if(error_map.size()==0){
			WebClient.create("http://localhost:9002/crm_management/services"
					+ "/customerService/registerCustomer")
			.type(MediaType.APPLICATION_JSON).post(model);
			System.out.println("注册成功...");
			
			//将邮箱激活码存入redis中
			String mailActiveCode = RandomStringUtils.randomNumeric(16);   //生成邮箱激活码
			//将邮箱激活码存入redis中
			redisTemplate.opsForValue().set(model.getTelephone(), mailActiveCode, 24, TimeUnit.HOURS);  
			String content = "<a href='"+MailUtils.activeUrl+"?telephone="+model.getTelephone()
					+"&mailActiveCode="+mailActiveCode+"'>邮箱激活链接，请于24小时内激活...</a>";
			MailUtils.sendMail(MailUtils.activeUrl, content, model.getEmail(), mailActiveCode);
			return SUCCESS;
		}else{
			System.out.println("注册失败...");
			return INPUT;
		}
		
	}
	//属性驱动  从激活链接中获取激活码
	private String mailActiveCode;
	
	public void setMailActiveCode(String mailActiveCode) {
		this.mailActiveCode = mailActiveCode;
	}
	@Action(value="customer_activeMail",results={@Result(name="success",location="login.html",type="redirect")})
	public String activeMail() throws IOException{
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");;
		String redis_mailActiveCode = redisTemplate.opsForValue().get(model.getTelephone());
		if(redis_mailActiveCode==null||!redis_mailActiveCode.equals(mailActiveCode)){
			ServletActionContext.getResponse().getWriter().print("验证码失效，请重新绑定邮箱...");
		}else{
			//防止重复激活
			Customer customer = WebClient.create("http://localhost:9002/crm_management/"
					+ "services/customerService/customer/telephone/"+model.getTelephone())
			.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if(customer.getType()==null||customer.getType()!=1){
				//没有绑定
				WebClient.create("http://localhost:9002/crm_management/"
						+ "services/customerService/customer/updateType/"+
						model.getTelephone()).put(null);
				System.out.println("邮箱激活成功...");
				//激活之后，清除redis相关缓存
				redisTemplate.delete(model.getTelephone());
				ServletActionContext.getResponse().getWriter().print("邮箱激活成功...");
						
			}else{
				//已经绑定，无需再次绑定
				System.out.println("已经绑定，无需再次绑定...");
				ServletActionContext.getResponse().getWriter().print("已经绑定，无需再次绑定...");
			}
		}
		return NONE;
		
	}
	//登录
	@Action(value="customer_login",results={
			@Result(name="success",location="index.html#/myhome",type="redirect"),
			@Result(name="input",location="login.html",type="redirect")})
	public String login(){
		System.out.println(Constant.CRM_MANAGEMENT_URL
				+"/services/customerService/customer_login?telephone="+model.getTelephone()+"&password="+model.getPassword());
		Customer customer = WebClient.create(Constant.CRM_MANAGEMENT_URL
				+"/services/customerService/customer_login?telephone="+model.getTelephone()+"&password="+model.getPassword())
				.accept(MediaType.APPLICATION_JSON)
				.get(Customer.class);
		if(customer!=null){
			//将登陆信息存入session中
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			System.err.println("登录成功....");
			return SUCCESS;
		}else{
			return INPUT;
		}
	}
}
