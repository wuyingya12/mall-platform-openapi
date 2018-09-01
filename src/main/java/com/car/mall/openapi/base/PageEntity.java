package com.car.mall.openapi.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

@ApiModel("分页对象")
public class PageEntity<T> {
    @ApiModelProperty(required = false, value = "总页数")
    private int totalPages;
    @ApiModelProperty(required = false, value = "总行数")
    private int totalRows;
    @ApiModelProperty(required = false, value = "当前页")
    private int currentPage;
    @ApiModelProperty(required = false, value = "每页最大行数")
    private int rows;
    @ApiModelProperty(required = false, value = "数据列表")
    private List<T> list;
    @ApiModelProperty(required = false, value = "错误信息")
    private String error;

    public PageEntity(){

    }
    /**
     * 一次查出全部的数据
     * @param list 全部数据
     */
    public PageEntity(List<T> list){
        this.list = list;
        int trows = 0;
        if(!CollectionUtils.isEmpty(this.list)){
            trows = this.list.size();
        }
        this.currentPage = 1;
        this.rows = trows;
        this.totalRows = trows;
        this.totalPages = 1;
    }
    public PageEntity(String error){
        this.error = error;
        this.currentPage = 1;
        this.rows = 0;
        this.totalRows = 0;
        this.totalPages = 1;
        this.list = Collections.emptyList();
    }
    /**
     *
     * @param currentPage 当前页
     * @param rows 每页最大行数
     * @param totalRows 总行数
     * @param list 当前数据列表
     */
    public PageEntity(int currentPage, int rows, int totalRows, List<T> list){
        this.currentPage = currentPage;
        this.rows = rows;
        this.totalRows = totalRows;
        this.list = list;
        int tp = 1;
        if(this.totalRows > 0){
            tp = (int)Math.ceil(this.totalRows /(this.rows * 1.0) );
        }
        this.totalPages = tp;
    }


    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
