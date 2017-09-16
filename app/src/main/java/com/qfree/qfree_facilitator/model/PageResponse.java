package com.qfree.qfree_facilitator.model;

import java.util.List;

/**
 * Created by Fahad Qureshi on 9/16/2017.
 */

public class PageResponse<T> {

    private List<T> docs;
    private Long total;
    private Long limit;
    private Long page;
    private Long pages;

    public PageResponse() {

    }

    public PageResponse(List<T> docs, Long total, Long limit, Long page, Long pages) {
        this.docs = docs;
        this.total = total;
        this.limit = limit;
        this.page = page;
        this.pages = pages;
    }

    public List<T> getDocs() {
        return docs;
    }

    public void setDocs(List<T> docs) {
        this.docs = docs;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }
}
