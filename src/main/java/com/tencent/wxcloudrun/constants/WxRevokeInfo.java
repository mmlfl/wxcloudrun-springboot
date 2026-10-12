package com.tencent.wxcloudrun.constants;

/**
 * RevokeInfo：用户撤回了哪一类授权信息。
 */
public final class WxRevokeInfo {

  private WxRevokeInfo() {
  }

  public static final String PLATE_NUMBER = "1";
  public static final String ADDRESS = "2";
  public static final String INVOICE = "3";
  public static final String BLUETOOTH = "4";
  public static final String MICROPHONE = "5";
  public static final String NICKNAME_AVATAR = "6";
  public static final String CAMERA = "7";
  public static final String PHONE = "8";
  public static final String WE_RUN = "12";
  public static final String LOCATION = "13";
  public static final String CHOSEN_MEDIA = "14";
  public static final String CHOSEN_FILE = "15";

  /** 邮箱地址 —— 不处理就会持续收到合规邮件 */
  public static final String EMAIL = "16";

  public static final String CHOSEN_LOCATION = "18";
  public static final String WECHAT_NICKNAME = "19";
  public static final String WECHAT_AVATAR = "20";

  public static String describe(String revokeInfo) {
    if (revokeInfo == null) {
      return "unknown";
    }
    switch (revokeInfo) {
      case PLATE_NUMBER:
        return "车牌号";
      case ADDRESS:
        return "地址";
      case INVOICE:
        return "发票信息";
      case BLUETOOTH:
        return "蓝牙";
      case MICROPHONE:
        return "麦克风";
      case NICKNAME_AVATAR:
        return "昵称和头像";
      case CAMERA:
        return "摄像头";
      case PHONE:
        return "手机号";
      case WE_RUN:
        return "微信运动步数";
      case LOCATION:
        return "位置信息";
      case CHOSEN_MEDIA:
        return "选中的图片或视频";
      case CHOSEN_FILE:
        return "选中的文件";
      case EMAIL:
        return "邮箱地址";
      case CHOSEN_LOCATION:
        return "选择的位置信息";
      case WECHAT_NICKNAME:
        return "微信昵称";
      case WECHAT_AVATAR:
        return "微信头像";
      default:
        return "unknown(" + revokeInfo + ")";
    }
  }
}
