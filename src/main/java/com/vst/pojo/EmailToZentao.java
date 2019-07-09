package com.vst.pojo;

import java.util.Date;

public class EmailToZentao {

	private Integer id;
	private Integer bugId;
	private String mailSubject;
	private String mailContent;
	private String emlFile;
	private Byte status;
	private Date createTime;
	private Date updateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBugId() {
		return bugId;
	}
	public void setBugId(Integer bugId) {
		this.bugId = bugId;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public String getEmlFile() {
		return emlFile;
	}
	public void setEmlFile(String emlFile) {
		this.emlFile = emlFile;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return "EmailToZentao [id=" + id + ", bugID=" + bugId + ", mailSubject=" + mailSubject + ", mailContent="
				+ mailContent + ", emlFile=" + emlFile + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}
}
