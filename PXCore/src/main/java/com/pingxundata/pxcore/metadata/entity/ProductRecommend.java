package com.pingxundata.pxcore.metadata.entity;

/**
* @Title: ProductRecommend.java
* @Description: 点击申请后弹出推荐位实体
* @author Away
* @date 2017/10/20 16:08
* @copyright 重庆平讯数据
* @version V1.0
*/
public class ProductRecommend {

    private Long id;//产品id
    private String name;//产品名称
    private String img;//产品图标
    private String recommendImg;//产品推荐图片
    private Double serviceRate;//产品利率
    private Integer startPeriod;//开始期限
    private Integer endPeriod;//结束期限
    private String periodType;//期限类型
    private Double startAmount;//
    private Double endAmount;
    private Boolean isValid=Boolean.TRUE;
    private Integer viewNum;//查看次数
    private Integer clickNum;//申请次数
    private Integer applyNum;
    private String productFlag;
    private String productLabel;
    private Integer isRecommend;//是否推荐
    private String productDesc;
    private String url;
    //合作模式
    private String cooperationModel;
    //合作价格
    private String unitPrice;
    private String onlineDate;
    private Integer loanDay;
    private Boolean isDownloadApp=Boolean.FALSE;
    private Double loanAmount=0.0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRecommendImg() {
        return recommendImg;
    }

    public void setRecommendImg(String recommendImg) {
        this.recommendImg = recommendImg;
    }

    public Double getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(Double serviceRate) {
        this.serviceRate = serviceRate;
    }

    public Integer getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Integer startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Integer getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(Integer endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Double getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(Double startAmount) {
        this.startAmount = startAmount;
    }

    public Double getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(Double endAmount) {
        this.endAmount = endAmount;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getClickNum() {
        return clickNum;
    }

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
    }

    public Integer getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(Integer applyNum) {
        this.applyNum = applyNum;
    }

    public String getProductFlag() {
        return productFlag;
    }

    public void setProductFlag(String productFlag) {
        this.productFlag = productFlag;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCooperationModel() {
        return cooperationModel;
    }

    public void setCooperationModel(String cooperationModel) {
        this.cooperationModel = cooperationModel;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(String onlineDate) {
        this.onlineDate = onlineDate;
    }

    public Integer getLoanDay() {
        return loanDay;
    }

    public void setLoanDay(Integer loanDay) {
        this.loanDay = loanDay;
    }

    public Boolean getDownloadApp() {
        return isDownloadApp;
    }

    public void setDownloadApp(Boolean downloadApp) {
        isDownloadApp = downloadApp;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }
}
