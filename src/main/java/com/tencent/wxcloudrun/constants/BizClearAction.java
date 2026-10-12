package com.tencent.wxcloudrun.constants;

/**
 * 通知 A104527 清理 SYSUSER 的动作码。
 *
 * <p>对照 FacialService 微信登录写入 SYSUSER 的字段：
 * openid、mobile/tel；表上还有 EMAIL（合规常见）。
 * displayName 来自身份证姓名，不是微信昵称，撤回昵称头像时不要清。</p>
 */
public final class BizClearAction {

  private BizClearAction() {
  }

  /** RevokeInfo=8：清 SYSUSER.MOBILE / TEL */
  public static final String CLEAR_PHONE = "CLEAR_PHONE";

  /** RevokeInfo=16：清 SYSUSER.EMAIL */
  public static final String CLEAR_EMAIL = "CLEAR_EMAIL";

  /** 解绑微信：清 SYSUSER.OPENID */
  public static final String UNBIND_OPENID = "UNBIND_OPENID";

  /** 用户注销：按合规清个人信息 + 解绑 openid */
  public static final String CLEAR_ALL = "CLEAR_ALL";
}
