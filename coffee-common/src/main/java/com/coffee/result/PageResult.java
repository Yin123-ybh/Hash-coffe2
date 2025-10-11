package com.coffee.result;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
public class PageResult implements Serializable {

    private static final long serialVersionUID = 1L;

    //总记录数
    private long total;

    //当前页数据集合
    private List records;

    public PageResult() {
    }

    public PageResult(long total, List records) {
        this.total = total;
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRecords() {
        return records;
    }

    public void setRecords(List records) {
        this.records = records;
    }
}