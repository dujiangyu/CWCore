package com.pingxundata.pxcore.metadata.enums;


/**
 * 市场名称编码
 */
public enum  ENUM_SOURCE_MARKET_CODE {

    /**
     * 百度
     */
    BAIDU("baidu"),
    /**
     * 小米
     */
    XIAOMI("xiaomi"),
    /**
     * vivo
     */
    VIVO("vivo"),
    /**
     * oppo
     */
    OPPO("oppo"),
    /**
     * 应用宝
     */
    YINGYONGBAO("yingyongbao"),
    /**
     * 豌豆荚
     */
    WANDOUJIA("wandoujia"),

    /**
     * 魅族
     */
    MEIZU("meizu"),

    /**
     * 360
     */
    S360("360"),

    /**
     * 华为
     */
    HUAWEI("huawei"),
    /**
     * 三星
     */
    SAMSUNG("Samsung"),
    /**
     * 安智
     */
    ANZHI("anzhi"),
    /**
     * 联想
     */
    LENOVO("Lenovo"),
    /**
     * 搜狗
     */
    SOGO("sogo"),
    /**
     * 锤子
     */
    HAMMER("hammer"),
    /**
     * 乐视
     */
    LESHI("leshi"),

    /**
     * 机锋
     */
    JIFENG("jifeng"),

    /**
     * 木蚂蚁
     */
    MUMAYI("mumayi"),

    /**
     * 应用汇
     */
    YINGYONGHUI("yingyonghui"),
    /**
     * 安卓园
     */
    ANZHUOYUAN("anzhuoyuan"),
    /**
     * 安粉网
     */
    ANFENWANG("anfenwang"),
    /**
     * 安贝市场
     */
    ANBEISHICHANG("anbeishichang"),
    /**
     * PC6下载站
     */
    PC6("pc6"),
    /**
     * 安卓市场
     */
    ANZHUOSHICHANG("anzhuoshichang"),
    /**
     * 91助手
     */
    ZHUSHOU91("91zhushou"),
    /**
     * 官方版本
     */
    OFFICIAL("official"),
    /**
     * 金立
     */
    JINLI("jinli"),
    /**
     * 优亿
     */
    YOUYI("youyi");

    public final String value;

    private ENUM_SOURCE_MARKET_CODE(String value){
        this.value=value;
    }
}
