package com.song.community.utils;

import org.springframework.stereotype.Component;

/**
 * @author Kycni
 * @date 2022/2/21 7:09
 */
@Component
public class Page {
    private int current = 1;
    private int limit = 10;
    private int rows;
    private String path;
    
    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current > 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit > 0 && limit < 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows > 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public int getOffset() {
        return (current-1) * limit;
    }
    
    public int getTotal() {
        if (rows%limit==0) {
            return rows/limit;
        } else {
            return (rows/limit) + 1;
        }
    }
    
    public int getFrom () {
        return Math.max(current - 2, 1);
    }
    
    public int getTo () {
        return Math.min(current + 2, getTotal());
    }
}
