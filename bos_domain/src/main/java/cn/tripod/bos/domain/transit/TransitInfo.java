package cn.tripod.bos.domain.transit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.tripod.bos.domain.takeDelivery.WayBill;


/**
 * @description: 运输配送信息
 */
@Entity
@Table(name = "T_TRANSIT_INFO")
public class TransitInfo {
	@Id
	@GeneratedValue
	@Column(name = "C_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "C_WAYBILL_ID")
	private WayBill wayBill;

	@OneToMany
	@JoinColumn(name = "C_TRANSIT_INFO_ID")
	@OrderColumn(name = "C_IN_OUT_INDEX")
	private List<InOutStorageInfo> inOutStorageInfos = new ArrayList<InOutStorageInfo>();

	@OneToOne
	@JoinColumn(name = "C_DELIVERY_INFO_ID")
	private DeliveryInfo deliveryInfo;

	@OneToOne
	@JoinColumn(name = "C_SIGN_INFO_ID")
	private SignInfo signInfo;

	@Column(name = "C_STATUS")
	// 出入库中转、到达网点、开始配送、正常签收、异常
	private String status;

	@Column(name = "C_OUTLET_ADDRESS")
	private String outletAddress;
	//物流信息
	@Transient
	public String getTransferInfo(){
		StringBuffer buffer = new StringBuffer();
		if(inOutStorageInfos.size()>0){
			for (InOutStorageInfo info : inOutStorageInfos) {
				buffer.append(info.getDescription()+"<br/>");
			}
		}
		if(deliveryInfo!=null){
			buffer.append(deliveryInfo.getDescription());
		}
		if(signInfo!=null){
			buffer.append(signInfo.getDescription());
		}
		System.err.println(buffer.toString());
		System.err.println(signInfo);
		return buffer.toString();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WayBill getWayBill() {
		return wayBill;
	}

	public void setWayBill(WayBill wayBill) {
		this.wayBill = wayBill;
	}

	public List<InOutStorageInfo> getInOutStorageInfos() {
		return inOutStorageInfos;
	}

	public void setInOutStorageInfos(List<InOutStorageInfo> inOutStorageInfos) {
		this.inOutStorageInfos = inOutStorageInfos;
	}

	public DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public SignInfo getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(SignInfo signInfo) {
		this.signInfo = signInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOutletAddress() {
		return outletAddress;
	}

	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}

	@Override
	public String toString() {
		return "TransitInfo [id=" + id + ", wayBill=" + wayBill
				+ ", inOutStorageInfos=" + inOutStorageInfos
				+ ", deliveryInfo=" + deliveryInfo + ", signInfo=" + signInfo
				+ ", status=" + status + ", outletAddress=" + outletAddress
				+ "]";
	}
	
}
