package org.demo.netty.test;

public class Request {
	private int id;
	private String name;
	private String reqeustMessag;
	private byte[] attachment;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReqeustMessag() {
		return reqeustMessag;
	}
	public void setReqeustMessag(String requestMessag) {
		this.reqeustMessag = requestMessag;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	
	

}
