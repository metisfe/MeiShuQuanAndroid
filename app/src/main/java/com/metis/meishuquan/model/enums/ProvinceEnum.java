package com.metis.meishuquan.model.enums;

/**
 * Created by WJ on 2015/5/12.
 */
public enum ProvinceEnum {
    BEIJING (11, "北京"), ZHEJIANG (33, "浙江(杭州)"), GUANGDONG (44, "广东"), CHONGQING (50, "重庆"),
    SHANDONG (37, "山东"), SICHUAN (51, "山东"), HUNAN(43, "湖南"), HUBEI(42, "湖北"), HEBEI(13, "河北"),
    TIANJIN (12, "天津"), SHANXI (14, "山西"), NEIMENGGU (15, "内蒙古"), LIAONING(21, "辽宁"), JILIN (22, "吉林"),
    HEILONGJIANG (23, "黑龙江"), SHANGHAI (31, "上海"), JIANGSU (32, "江苏"), ANHUI (34, "安徽"), FUJIAN (35, "福建"), JIANGXI (36, "江西"),
    HENAN (41, "河南"), GUANGXI (45, "广西"), HAINAN (46, "海南"), GUIZHOU (52, "贵州"), YUNNAN (53, "云南"), XIZANG (54, "西藏"),
    SHANXI2 (61, "陕西"), GANSU (62, "甘肃"), QINGHAI (63, "青海"), NINGXIA (64, "宁夏"), XINJIANG (65, "新疆"), TAIWAN (71, "台湾"),
    XIANGGANG (81, "香港"), AOMEN (91, "澳门");
    private int provinceId;
    private String name;

    ProvinceEnum (int id, String name) {
        provinceId = id;
        this.name = name;
    }

    public int getId () {
        return provinceId;
    }

    public String getName () {
        return name;
    }
}
