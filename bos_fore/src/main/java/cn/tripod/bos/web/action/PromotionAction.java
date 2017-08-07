package cn.tripod.bos.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;














import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.tripod.bos.domain.takeDelivery.Constant;
import cn.tripod.bos.domain.takeDelivery.PageBean;
import cn.tripod.bos.domain.takeDelivery.Promotion;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {
	
	//活动页显示
	@Action(value="promotion_queryPage",results={@Result(name="success",type="json")})
	public String queryPage(){
		PageBean pageBean = WebClient.create("http://localhost:8080/bos_management/services/"
				+ "promotionService/pageQuery?page="+page+"&rows="+rows)
				.accept(MediaType.APPLICATION_JSON)
				.get(PageBean.class);
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}
	
	//静态页面加载详情
	@Action(value="promotion_showDetail")
	public String showDetail() throws IOException, TemplateException{
		String templateRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
		File htmlFile = new File(templateRealPath+"/"+model.getId()+".html");
		if(!htmlFile.exists()){
			//访问的页面不存在
			//freemarker静态页面技术的使用
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
			//设置原始模板路径
			configuration.setDirectoryForTemplateLoading(
					new File(ServletActionContext.getServletContext()
							.getRealPath("/WEB-INF/freemarker_template")));
			//获取模板对象
			Template template = configuration.getTemplate("promotion_detail.ftl");
			//动态数据
			Promotion promotion = WebClient.create(Constant.BOS_MANAGEMENT_URL+"/bos_management/services/"
				+ "promotionService/getPromotion/"+model.getId())
			.accept(MediaType.APPLICATION_JSON)
			.get(Promotion.class);
			Map<String, Object> paramterMap = new HashMap<String, Object>();
			paramterMap.put("promotion", promotion);
			//合并输出流
			template.process(paramterMap, new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));
			
		}else{
			//访问的页面存在
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
		}
		return NONE;
	}

}
