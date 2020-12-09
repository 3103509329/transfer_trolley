package com.zhcx.authorization.socketIO;

public class MessgeBean {
	private String from;
	private String to;
	private Object content;
	private String type;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "MsgBean [from=" + from + ", to=" + to + ", content=" + content + "]";
	}
}
