package cn.tripod.bos.web.action.report;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.tripod.bos.domain.takeDelivery.WayBill;
import cn.tripod.bos.service.takeDelivery.WayBillService;
import cn.tripod.bos.utils.FileUtils;
import cn.tripod.bos.web.action.common.BaseAction;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class ReportAction extends BaseAction<WayBill> {
	@Autowired
	private WayBillService wayBillService;
	@Autowired
	private DataSource dataSource;
	@Action(value = "report_exportXls")
	public String exportXls() throws IOException {

		List<WayBill> list = wayBillService.findAll(model);
		@SuppressWarnings("resource")
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();// 创建文件处理对象
		HSSFSheet sheet = hssfWorkbook.createSheet();// 生成sheet
		HSSFRow row = sheet.createRow(0); // 创建表头对象，且对表头赋值
		row.createCell(0).setCellValue("运单编号");
		row.createCell(1).setCellValue("寄件人");
		row.createCell(2).setCellValue("寄件人电话");
		row.createCell(3).setCellValue("寄件人地址");
		row.createCell(4).setCellValue("收件人");
		row.createCell(5).setCellValue("收件人电话");
		row.createCell(6).setCellValue("收件人地址");

		for (WayBill wayBill : list) {
			HSSFRow _row = sheet.createRow(sheet.getLastRowNum() + 1);
			_row.createCell(0).setCellValue(wayBill.getWayBillNum());
			_row.createCell(1).setCellValue(wayBill.getSendName());
			_row.createCell(2).setCellValue(wayBill.getSendMobile());
			_row.createCell(3).setCellValue(wayBill.getSendAddress());
			_row.createCell(4).setCellValue(wayBill.getRecName());
			_row.createCell(5).setCellValue(wayBill.getRecMobile());
			_row.createCell(6).setCellValue(wayBill.getRecAddress());
		}
		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType(
				"application/vnd.ms-excel");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String filename = "运单数据_" + dateFormat.format(new Date()) + ".xls";
		String agent = ServletActionContext.getRequest()
				.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition",
				"attachment;filename=" + filename);
		ServletOutputStream outputStream = ServletActionContext.getResponse()
				.getOutputStream();
		hssfWorkbook.write(outputStream);
		// 关闭hssfWorkbook
		hssfWorkbook.close();
		return NONE;
	}

	@Action(value = "report_exportPdf")
	public String exportPdf() throws IOException, DocumentException {
		List<WayBill> list = wayBillService.findAll(model);
		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType(
				"application/pdf");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String filename = "运单数据_" + dateFormat.format(new Date()) + ".pdf";
		String agent = ServletActionContext.getRequest()
				.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition",
				"attachment;filename=" + filename);

		Document document = new Document();
		PdfWriter.getInstance(document, ServletActionContext.getResponse().getOutputStream());
		document.open();
		
		// 向document 生成pdf表格
		Table table = new Table(7);
		table.setWidth(100); // 宽度
		table.setBorder(1); // 边框
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式

		// 设置表格字体
		BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);

		// 向表格写数据
		// 表头
		table.addCell(buildCell("运单编号", font));
		table.addCell(buildCell("发件人", font));
		table.addCell(buildCell("发件人电话", font));
		table.addCell(buildCell("发件人地址", font));
		table.addCell(buildCell("收件人", font));
		table.addCell(buildCell("收件人电话", font));
		table.addCell(buildCell("收件人地址", font));

		// 表格数据
		for (WayBill wayBill : list) {
			table.addCell(buildCell(wayBill.getWayBillNum(), font));
			table.addCell(buildCell(wayBill.getSendName(), font));
			table.addCell(buildCell(wayBill.getSendMobile(), font));
			table.addCell(buildCell(wayBill.getSendAddress(), font));
			table.addCell(buildCell(wayBill.getRecName(), font));
			table.addCell(buildCell(wayBill.getRecMobile(), font));
			table.addCell(buildCell(wayBill.getRecAddress(), font));
		}

		// 向文档添加表格
		document.add(table);
		document.addTitle("运单数据");
		//关闭文档资源
		document.close();
		return NONE;
	}
	//创建表格方法
	private Cell buildCell(String content, Font font) throws BadElementException {
		Phrase phrase = new Phrase(content, font);
		return new Cell(phrase);
	}
	
	@Action(value="report_exportJasperPdf")
	public String exportJasperPdf() throws IOException, JRException, SQLException{
		List<WayBill> list = wayBillService.findAll(model);
		// 下载导出
		// 设置头信息
		ServletActionContext.getResponse().setContentType(
				"application/pdf");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String filename = "运单数据_" + dateFormat.format(new Date()) + ".pdf";
		String agent = ServletActionContext.getRequest()
				.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition",
				"attachment;filename=" + filename);
		String jrxml = ServletActionContext.getServletContext().getRealPath("/WEB-INF/jasper/waybill.jrxml");
		// 准备需要数据
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("company", "Tripod");
		// 准备需要数据
		JasperReport report = JasperCompileManager.compileReport(jrxml);
		JasperPrint jasperPrint = 
				JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(list));

		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		// 使用JRPdfExproter导出器导出pdf
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		exporter.exportReport();// 导出
		outputStream.close();// 关闭流
		return NONE;
	}
}
