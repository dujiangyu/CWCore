package com.pingxundata.pxcore.metadata.enums;


/**
 * 请求路径枚举
 */
public class ENUM_REQUEST_URL {

    /**
     * 域名
     */
    public static String DOMAIN="http://120.79.255.186";

    /**
     * 请求路径
     */
    public final static String LOGIN="/common/passwordLogin.json";

    /**
     * 发送验证码
     */
    public final static String SENDSMSVERIFY="/common/sendSmsVerify.json";

    /**
     *查询详情
     */
    public final static String PRODUCTDETDETAILS="/front/product/findById.json";

    /**
     * 产品搜索
     */
    public final static String PRODUCTSEARCH="/front/product/findByCondition.json";

    /**
     * 查找所有产品
     */
    public final static String PRODUCTSEARCHALL="/front/product/findAll.json";

    /**
     * 产品是否更新
     */
    public final static String PRODUCTISUPDATE="/front/product/findProductVersion.json";

    /**
     * 首页初始数据
     */
    public final static String INITPARAM="/front/parameter/findParameter.json";

    /**
     * 立即申请数据埋点
     */
    public final static String DATAPOINT="/front/product/applyLoan.json";

    /**
     * 通用下拉列表数据
     */
    public final static String SIMPLCODE="/common/findByType.json";

    /**
     * 消息列表
     */
    public final static String MESSAGE_LIST="/front/message/list.json";

    /**
     * 消息详情
     */
    public final static String MESSAGE_BYID="/front/message/findById.json";

    /**
     * 申请记录
     */
    public final static String APPLYRECORD="/front/product/findApplyList.json";

    /**
     * Banner
     */
    public final static String BANNER="/common/findBanner.json";

    /**
     * 信用卡推荐
     */
    public final static String CREDIT_INS="/front/creditCard/findRecommend.json";

    /**
     * 信用卡列表数据
     */
    public final static String CREDIT_PRODUCTS="/front/creditCard/findByCondition.json";

    /**
     * 产品更新信息
     */
    public final static String VERSION="/front/product/findProductVersion.json";

    /**
     * 客户信息
     */
    public final static String CLIENT_DETAIL="/front/userInfo/findById.json";

    /**
     * 客户信息修改
     */
    public final static String CLIENT_DETAIL_EDIT="/front/userInfo/update.json";

    /**
     * 信用卡申请
     */
    public final static String CREDITCARD_APPLY="/front/creditCard/applyCreditCard.json";

    /**
     * 模块开关
     */
    public final static String APP_MODULE="/front/sys/getAppModule.json";

    /**
     * 查找产品推荐
     */
    public final static String PRODUCT_RECOMMEND="/front/product/findRecommendProduct.json";

    /**
     * 攻略列表
     */
    public final static String STRATEGY_LIST="/common/cms/findByCondition.json";

    /**
     * 攻略详情
     */
    public final static String STRATEGY_DETAIL="/common/cms/findById.json";

    /**
     * 跑马灯数据
     */
    public final static String APPLY_RESULT="/front/product/findApplyResult.json";

    /**
     * 产品申请推荐
     */
    public final static String APPLY_RECOMMEND="/front/product/findApplyProductRecommend.json";

    /**
     * 积分墙
     */
    public final static String WALL="/common/verifyIdfaValid.json";

    /**
     * 退出登录
     */
    public final static String LOGOUT="/common/logout.json";

}
