package com.zhcx.netcar.netcarservice.utils.excel;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExcelItem implements Serializable {
	private static final long serialVersionUID = -8617091810386157414L;
	String code;
	String name ;
	String childName;

}
