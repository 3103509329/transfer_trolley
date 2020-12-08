package com.zhcx.netcar.netcarservice.utils.excel;

import io.netty.util.internal.StringUtil;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ExcelItemUtil {

	public static List<ExcelItem> convertToExcelItemList(String codes[],String names[]){
		List<ExcelItem> list = new ArrayList<ExcelItem>();
		for(int i=0;i<codes.length;i++) {
			ExcelItem item = new ExcelItem();
			item.setCode(codes[i]);
			item.setName(names[i]);
			list.add(item);
		}
		return list;
	}
	
	
	public static List<ExcelItem> convertToExcelItemList(String itemJosn){
		if(StringUtil.isNullOrEmpty(itemJosn))
			return null;
		JSONArray jsonArray = JSONArray.fromObject(itemJosn);
		List<String> jsonList =(List<String>) jsonArray.toCollection(jsonArray, String.class);
		List<ExcelItem> list = new ArrayList<ExcelItem>();
		for(String itemStr: jsonList) {
			String[] proValue = itemStr.split(",");
			if(proValue.length>=1) {
				ExcelItem item = new ExcelItem();
				item.setCode(proValue[0]);
				if(proValue.length>=2) {
					item.setName(proValue[1]);
				}
				if(proValue.length>=3) {
					item.setChildName(proValue[2]);
				}
				list.add(item);
			}
		}
//		List<ExcelItem> list = (List<ExcelItem>) jsonArray.toCollection(jsonArray, ExcelItem.class);
		return list;
	}
}
