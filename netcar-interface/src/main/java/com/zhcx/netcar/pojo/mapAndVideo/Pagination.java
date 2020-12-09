package com.zhcx.netcar.pojo.mapAndVideo;//package com.zhcx.netcarbasic.pojo.mapAndVideo;
//
//import jnr.ffi.annotations.In;
//
//import java.io.Serializable;
//import java.util.Objects;
//
///**
// * @author Administrator
// * @email 570815140@qq.com
// * @date 2019/6/1 0001 11:27
// **/
//public class Pagination implements Serializable {
//    private static final long serialVersionUID = 1737523952311730799L;
//    public Integer totalPages;
//    public Integer currentPage;
//    public Integer pageRecords;
//    public Integer totalRecords;
//    public Integer sortParams;
//    public Boolean hasNextPage;
//    public Boolean hasPreviousPage;
//    public Integer nextPage;
//    public Integer previousPage;
//    public Integer startRecord;
//
//    @Override
//    public String toString() {
//        return "Pagination{" +
//                "totalPages=" + totalPages +
//                ", currentPage=" + currentPage +
//                ", pageRecords=" + pageRecords +
//                ", totalRecords=" + totalRecords +
//                ", sortParams=" + sortParams +
//                ", hasNextPage=" + hasNextPage +
//                ", hasPreviousPage=" + hasPreviousPage +
//                ", nextPage=" + nextPage +
//                ", previousPage=" + previousPage +
//                ", startRecord=" + startRecord +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Pagination that = (Pagination) o;
//        return Objects.equals(totalPages, that.totalPages) &&
//                Objects.equals(currentPage, that.currentPage) &&
//                Objects.equals(pageRecords, that.pageRecords) &&
//                Objects.equals(totalRecords, that.totalRecords) &&
//                Objects.equals(sortParams, that.sortParams) &&
//                Objects.equals(hasNextPage, that.hasNextPage) &&
//                Objects.equals(hasPreviousPage, that.hasPreviousPage) &&
//                Objects.equals(nextPage, that.nextPage) &&
//                Objects.equals(previousPage, that.previousPage) &&
//                Objects.equals(startRecord, that.startRecord);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(totalPages, currentPage, pageRecords, totalRecords, sortParams, hasNextPage, hasPreviousPage, nextPage, previousPage, startRecord);
//    }
//
//    public Integer getTotalPages() {
//        return totalPages;
//    }
//
//    public void setTotalPages(Integer totalPages) {
//        this.totalPages = totalPages;
//    }
//
//    public Integer getCurrentPage() {
//        return currentPage;
//    }
//
//    public void setCurrentPage(Integer currentPage) {
//        this.currentPage = currentPage;
//    }
//
//    public Integer getPageRecords() {
//        return pageRecords;
//    }
//
//    public void setPageRecords(Integer pageRecords) {
//        this.pageRecords = pageRecords;
//    }
//
//    public Integer getTotalRecords() {
//        return totalRecords;
//    }
//
//    public void setTotalRecords(Integer totalRecords) {
//        this.totalRecords = totalRecords;
//    }
//
//    public Integer getSortParams() {
//        return sortParams;
//    }
//
//    public void setSortParams(Integer sortParams) {
//        this.sortParams = sortParams;
//    }
//
//    public Boolean getHasNextPage() {
//        return hasNextPage;
//    }
//
//    public void setHasNextPage(Boolean hasNextPage) {
//        this.hasNextPage = hasNextPage;
//    }
//
//    public Boolean getHasPreviousPage() {
//        return hasPreviousPage;
//    }
//
//    public void setHasPreviousPage(Boolean hasPreviousPage) {
//        this.hasPreviousPage = hasPreviousPage;
//    }
//
//    public Integer getNextPage() {
//        return nextPage;
//    }
//
//    public void setNextPage(Integer nextPage) {
//        this.nextPage = nextPage;
//    }
//
//    public Integer getPreviousPage() {
//        return previousPage;
//    }
//
//    public void setPreviousPage(Integer previousPage) {
//        this.previousPage = previousPage;
//    }
//
//    public Integer getStartRecord() {
//        return startRecord;
//    }
//
//    public void setStartRecord(Integer startRecord) {
//        this.startRecord = startRecord;
//    }
//}
