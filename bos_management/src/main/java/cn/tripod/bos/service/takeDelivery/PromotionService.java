package cn.tripod.bos.service.takeDelivery;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.tripod.bos.domain.takeDelivery.PageBean;
import cn.tripod.bos.domain.takeDelivery.Promotion;

public interface PromotionService {

	void save(Promotion model);
	
	@Path("/pageQuery")
	@GET
	@Produces({"application/xml","application/json"})
	PageBean<Promotion> findPage(@QueryParam("page") int page ,@QueryParam("rows") int rows);
	
	@Path("/getPromotion/{id}")
	@GET
	//@Consumes({"application/xml","application/json"})
	@Produces({"application/xml","application/json"})
	Promotion findPromotion(@PathParam("id") Integer id);

	void updateType(Date date);
}
