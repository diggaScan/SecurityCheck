package com.sunland.securitycheck;

public class DataModel {

    public static String[] conference_locs = {"G20主会场", "B20主会场", "西湖安保圈", "黄龙板块", "西溪板块", "萧山机场",
            "省军区", "记者驻地", "滨江安保"};

    public static String[] gender = {"男", "女"};

    public static String[] CERTIFICATIONS = {
            "居民身份证", "临时身份证", "护照",
            "港澳同胞回乡证", "港澳居民来往内地通行证", "台湾居民来往大陆通行证",
            "武警警官证", "军官证", "军队学员证", "军队文职干部证",
            "军队离退休干部证", "士兵证", "领事馆证", "外国人出入境证",
            "外国人居留证", "外交官证"
    };

    public static final String[] NATIONS = {
            "中国", "阿根廷", "爱尔兰", "奥地利", "澳大利亚",
            "巴基斯坦", "巴西", "白俄罗斯", "保加利亚", "比利时",
            "波兰", "不丹", "丹麦", "德国", "俄罗斯", "法国", "菲律宾", "刚果", "哥伦比亚",
            "哥斯达黎加", "古巴", "韩国", "荷兰", "华侨", "加拿大",
            "加纳", "捷克", "津巴布韦", "拉脱维亚", "马来西亚", "毛里求斯",
            "美国", "蒙古", "孟加拉", "摩洛哥", "墨西哥", "尼泊尔", "日本", "瑞典", "瑞士",
            "塞尔维亚", "斯洛伐克", "斯洛文尼亚", "泰国", "突尼斯", "委内瑞拉", "乌克兰",
            "西班牙", "新加坡", "新西兰", "匈牙利", "牙买加", "伊朗", "意大利", "印度",
            "印度尼西亚", "英国", "越南", "中国澳门", "中国台湾", "中国香港", " 智利",
            "秘鲁", "爱沙尼亚", " 哈萨克斯坦", " 罗马尼亚", "尼日利亚",
    };


    public static final String ACTION_NFC_READ_IDCARD_SUCCESS  =
            "cybertech.pstore.intent.action.NFC_READ_IDCARD_SUCCESS";

    public static final String ACTION_NFC_READ_IDCARD_FAILURE  =
            "cybertech.pstore.intent.action.NFC_READ_IDCARD_FAILURE";

    public static final String[] AREA_CODE={"01","02","03","04","05","06","07","08","09","10"};

}
