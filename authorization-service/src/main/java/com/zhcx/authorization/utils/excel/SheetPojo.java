package com.zhcx.authorization.utils.excel;

import lombok.Data;

/**
 * @ClassName：Sheet
 * @Description: 列表导出
 * @author：李亮
 * @date：2020/7/615:33
 */
@Data
public class SheetPojo {

    /**
     * sheet名称
     */
    String sheetName;

    /**
     * 列名称
     */
    String[] title;

    /**
     * 数据
     */
    String[][] values;

    /**
     * 表格大标题
     */
    String headline;

    /**
     * 数据导出类型（0.普通，1.带大标题）
     */
    Integer type;
}
