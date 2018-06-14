package com.pingxundata.pxcore.metadata.entity;

/**
 * bannner图片
 */
public class BannerImg {

    private int pageNo;
    private int sizePerPage;
    private String sortDirection;
    private String sortFields;
    private String id;
    private String name;
    private String bannerImg;
    private String bannerPosition;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getSizePerPage() {
        return sizePerPage;
    }

    public void setSizePerPage(int sizePerPage) {
        this.sizePerPage = sizePerPage;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        this.sortFields = sortFields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getBannerPosition() {
        return bannerPosition;
    }

    public void setBannerPosition(String bannerPosition) {
        this.bannerPosition = bannerPosition;
    }
}
