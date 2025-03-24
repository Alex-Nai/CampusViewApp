package com.example.campusview.model;

import java.util.List;

public class PageResponse<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private boolean hasNext;

    public PageResponse(List<T> content, long totalElements, int totalPages, int pageNumber, int pageSize, boolean hasNext) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.hasNext = hasNext;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isHasNext() {
        return hasNext;
    }
} 