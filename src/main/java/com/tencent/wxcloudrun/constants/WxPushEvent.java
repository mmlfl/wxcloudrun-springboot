package com.tencent.wxcloudrun.constants;

/**
 * 微信消息推送 Event 枚举值（授权用户信息变更相关）。
 * 文档：授权用户信息变更 / authorized_user_information_changes
 */
public final class WxPushEvent {

  private WxPushEvent() {
  }

  /** 用户资料变更（头像昵称等风险清理） */
  public static final String USER_INFO_MODIFIED = "user_info_modified";

  /** 用户撤回授权 —— 你要处理的合规事件 */
  public static final String USER_AUTHORIZATION_REVOKE = "user_authorization_revoke";

  /** 用户完成注销 */
  public static final String USER_AUTHORIZATION_CANCELLATION = "user_authorization_cancellation";

  /** 云托管绑路径时的探测 action */
  public static final String CHECK_CONTAINER_PATH = "CheckContainerPath";

}
