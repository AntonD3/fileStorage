package com.example.fileStorage.dto;

import com.example.fileStorage.model.File;

import java.util.List;

public class FilePage {
    private long total;
    private List<File> page;

    public FilePage() {
    }

    public FilePage(long total, List<File> page) {
        this.total = total;
        this.page = page;
    }

    @Override
    public String toString() {
        return "FilePage{" +
                "total=" + total +
                ", page=" + page +
                '}';
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<File> getPage() {
        return page;
    }

    public void setPage(List<File> page) {
        this.page = page;
    }
}
