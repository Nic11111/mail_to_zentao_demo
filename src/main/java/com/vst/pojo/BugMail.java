package com.vst.pojo;

import java.util.Date;

import com.vst.constant.EventStatus;

public class BugMail {

	private Integer id;
	private Integer bugId;
	private String title;
	private String eml;
	private String fromAddress;
	private String cc;
	private String toAddress;
	private EventStatus emailStatus;
	private EventStatus zentaoStatus;
	private Date receiveTime;
	private Date lastReplyTime;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEml() {
		return eml;
	}
	public void setEml(String eml) {
		this.eml = eml;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public EventStatus getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(EventStatus emailStatus) {
		this.emailStatus = emailStatus;
	}
	public EventStatus getZentaoStatus() {
		return zentaoStatus;
	}
	public void setZentaoStatus(EventStatus zentaoStatus) {
		this.zentaoStatus = zentaoStatus;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public Date getLastReplyTime() {
		return lastReplyTime;
	}
	public void setLastReplyTime(Date lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}
	
	@Override
	public String toString() {
		return "BugMail [id=" + id + ", bugId=" + bugId + ", title=" + title + ", eml=" + eml + ", fromAddress="
				+ fromAddress + ", cc=" + cc + ", toAddress=" + toAddress + ", emailStatus=" + emailStatus
				+ ", zentaoStatus=" + zentaoStatus + ", receiveTime=" + receiveTime + ", lastReplyTime=" + lastReplyTime
				+ "]";
	}
}
