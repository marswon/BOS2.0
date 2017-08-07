package cn.tripod.bos.domain.takeDelivery;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
@XmlRootElement(name="pageBean")
@XmlSeeAlso({Promotion.class})
public class PageBean<T> {
	private long totalCount;
	private List<T> pageData;
	
	public long getTotalcount() {
		return totalCount;
	}
	public void setTotalcount(long totalcount) {
		this.totalCount = totalcount;
	}
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	
}
