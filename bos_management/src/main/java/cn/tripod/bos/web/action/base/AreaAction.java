package cn.tripod.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.base.Area;
import cn.tripod.bos.service.base.AreaService;
import cn.tripod.bos.utils.PinYin4jUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {
	private Area area = new Area();
	@Autowired
	private AreaService areaService;
	@Override
	public Area getModel() {
		return area;
	}
	//获取导入文件
	private File[] file;
	
	public void setFile(File[] file) {
		this.file = file;
	}

	//批量导入
	@Action(value="area_patchImport",results={@Result(name="success",location="/pages/base/area.html",type="redirect")})
	public String area_patchImport(){
		
			for (int i = 0; i < file.length; i++) {
				HSSFWorkbook hssfWorkbook;
				try {
					hssfWorkbook = new HSSFWorkbook(new FileInputStream(file[i]));
					HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
					List<Area> areaList = new ArrayList<Area>();
					for (Row row : sheet) {
						//跳过第一行表头
						if(row.getRowNum()==0){
							continue;
						}
						if(row.getCell(0)==null||StringUtils.isBlank(row.getCell(0).getStringCellValue())){
							continue;
						}
						Area area = new Area();
						area.setId(row.getCell(0).getStringCellValue());
						area.setProvince(row.getCell(1).getStringCellValue());
						area.setCity(row.getCell(2).getStringCellValue());
						area.setDistrict(row.getCell(3).getStringCellValue());
						area.setPostcode(row.getCell(4).getStringCellValue());
						
						String province = area.getProvince();
						String city = area.getCity();
						String district = area.getDistrict();
						province = province.substring(0, province.length()-1);
						city = city.substring(0, city.length()-1);
						district = district.substring(0, district.length()-1);
						//城市编码
						String citycode = PinYin4jUtils.hanziToPinyin(city, "");
						area.setCitycode(citycode);
						//简码
						String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
						StringBuffer sb = new StringBuffer();
						for (String str : headByString) {
							sb.append(str);
						}
						String shortcode = sb.toString();
						area.setShortcode(shortcode);
						areaList.add(area);
					}
					areaService.save(areaList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
		
		return SUCCESS;
	}
	//属性封装分页参数
	private Integer page;
	private Integer rows;
	
	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	//条件查询
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String area_pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		Specification<Area> specification = new Specification<Area>() {
			
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(area.getProvince())){
					Predicate p1 = cb.like(root.get("province").as(String.class), "%"+area.getProvince()+"%");
					list.add(p1);
				}
				if(StringUtils.isNotBlank(area.getCity())){
					Predicate p2 = cb.like(root.get("city").as(String.class), "%"+area.getCity()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(area.getDistrict())){
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+area.getDistrict()+"%");
					list.add(p3);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
	Page<Area> pageData = areaService.findPageData(specification,pageable);
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("total", pageData.getTotalElements());
	map.put("rows", pageData.getContent());
	ActionContext.getContext().getValueStack().push(map);
		
		return SUCCESS;
	}
}
