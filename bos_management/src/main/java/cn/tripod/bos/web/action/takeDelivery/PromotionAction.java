package cn.tripod.bos.web.action.takeDelivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.tripod.bos.domain.takeDelivery.Promotion;
import cn.tripod.bos.service.takeDelivery.PromotionService;
import cn.tripod.bos.web.action.common.BaseAction;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {
	@Autowired
	private PromotionService promotionService;
	
	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;
	
	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getImgFileFileName() {
		return imgFileFileName;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public String getImgFileContentType() {
		return imgFileContentType;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	//图片上传
	@Action(value="image_upload",results={@Result(name="success",type="json")})
	public String upload(){
		System.out.println("imgFile:"+imgFile);
		System.out.println("imgFileFileName:"+imgFileFileName);
		System.out.println("imgFileContentType:"+imgFileContentType);
		//带盘符的绝对路径
		String dirPath = ServletActionContext.getServletContext().getRealPath("/upload");
		//url绝对路径
		String urlPath = ServletActionContext.getRequest().getContextPath()+"/upload/";
		System.out.println(dirPath);
		System.out.println(urlPath);
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		try {
			FileUtils.copyFile(imgFile, new File(dirPath,uuid+imgFileFileName));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", 0);
			map.put("url", urlPath+uuid+imgFileFileName);
			ActionContext.getContext().getValueStack().push(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	//从服务器遍历图片
	@Action(value="image_manage",results={@Result(name="success",type="json")})
	public String manage(){
		//带盘符的绝对路径
		String rootPath = ServletActionContext.getServletContext().getRealPath("/upload");
		//url
		String urlPath = ServletActionContext.getRequest().getContextPath()+"/upload/";
		//文件类型
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		File currentPathFile = new File(rootPath);
		List<HashMap> fileList = new ArrayList<HashMap>();
		if(currentPathFile.listFiles() != null) {
			//遍历绝对路径
			for (File file : currentPathFile.listFiles()) {
				HashMap<String, Object> hash = new HashMap<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				
				fileList.add(hash);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", urlPath);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	//获取表格中文件
	private File titleImgFile;
	private String titleImgFileFileName;
	

	public File getTitleImgFile() {
		return titleImgFile;
	}

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public String getTitleImgFileFileName() {
		return titleImgFileFileName;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	//保存表格信息
	@Action(value="promotion_save",results={@Result(name="success",type="json")})
	public String save(){
		String dirPath = ServletActionContext.getServletContext().getRealPath("/upload");
		String urlPath = ServletActionContext.getRequest().getContextPath()+"/upload";
		//获取文件名称
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String _filename = uuid+"-"+titleImgFileFileName;
		String destPath = dirPath+"/"+_filename;
		//上传文件
		try {
			FileUtils.copyFile(titleImgFile, new File(destPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setTitleImg(urlPath+"/"+_filename);
		
		promotionService.save(model);
		return SUCCESS;
	}
}
