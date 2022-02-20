package com.song.community.utils;

import org.springframework.stereotype.Component;

/**
 * @author Kycni
 * @date 2022/2/20 10:39
 */
@Component
public class Page {
    /**
     * 分页
     * 分页 页面
     * 每一页的起始数据行数offset，页容limit
     * 分页 逻辑
     * 当前页current(大于0,小于总页数total)
     * 数据总行数rows
     * 总页数total -> 数据总行数rows/页面容量limit（+1）
     * 上一页prePage = current == 1 ? current : current - 1;
     * 下一页afterPage = current == total ? current : current + 1;
     * 通过类变量计算得到的属性,可以不用显性的声明,如offset,total
     * 分页路径path
     */

    private int limit = 10;
    private int current = 1;
    private int rows;
    private String path;

    public int getOffset() {
        return (current - 1) * limit;
    }

    public int getTotal() {
        return rows % limit == 0 ? (rows / limit) : (rows / limit) + 1;
    }
    
    public int getPrePage() {
        return current == 1 ? current : current - 1;
    }
    
    public int getAfterPage() {
        return current == getTotal() ? getTotal() : current + 1;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit > 0 && limit < 50) {
            this.limit = limit;
        }
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current > 0) {
            this.current = current;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
