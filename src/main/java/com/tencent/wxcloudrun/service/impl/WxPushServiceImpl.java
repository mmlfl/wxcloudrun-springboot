package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.client.BizServerClient;
import com.tencent.wxcloudrun.constants.BizClearAction;
import com.tencent.wxcloudrun.constants.WxPushEvent;
import com.tencent.wxcloudrun.constants.WxRevokeInfo;
import com.tencent.wxcloudrun.dto.WxPushMessage;
import com.tencent.wxcloudrun.service.WxPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 只处理与 A104527 SYSUSER「微信侧可能写入」相关的撤销。
 *
 * <p>FacialService 微信登录实际写入 SYSUSER：
 * <ul>
 *   <li>openid</li>
 *   <li>mobile / tel（微信手机号）</li>
 * </ul>
 * 表字段还有 EMAIL（合规邮件常见触发，一并处理）。
 * displayName 来自身份证姓名，不是微信昵称/头像，故 6/19/20 不清理 SYSUSER。</p>
 */
@Service
public class WxPushServiceImpl implements WxPushService {

  private final Logger logger = LoggerFactory.getLogger(WxPushServiceImpl.class);
  private final BizServerClient bizServerClient;

  public WxPushServiceImpl(@Autowired BizServerClient bizServerClient) {
    this.bizServerClient = bizServerClient;
  }

  @Override
  public void handle(WxPushMessage message) {
    if (message == null) {
      return;
    }

    if (WxPushEvent.CHECK_CONTAINER_PATH.equals(message.getAction())) {
      logger.info("收到云托管路径探测 CheckContainerPath");
      return;
    }

    String msgType = message.getMsgType();
    String event = message.getEvent();
    if (!StringUtils.hasText(msgType) || !StringUtils.hasText(event)) {
      logger.info("收到非事件推送 msgType={}, content={}", msgType, message.getContent());
      return;
    }
    if (!"event".equalsIgnoreCase(msgType)) {
      logger.info("忽略非 event 消息 msgType={}, msgId={}", msgType, message.getMsgId());
      return;
    }

    logger.info(
        "授权事件 event={}, openId={}, appId={}, revokeInfo={}({})",
        event,
        message.getOpenId(),
        message.getAppId(),
        message.getRevokeInfo(),
        WxRevokeInfo.describe(message.getRevokeInfo()));

    if (WxPushEvent.USER_AUTHORIZATION_REVOKE.equals(event)) {
      onUserAuthorizationRevoke(message);
    } else if (WxPushEvent.USER_INFO_MODIFIED.equals(event)) {
      // SYSUSER.displayName 是实名，不是微信资料；这里只记日志
      logger.info("user_info_modified：SYSUSER 无微信昵称/头像字段，跳过清理 openId={}", message.getOpenId());
    } else if (WxPushEvent.USER_AUTHORIZATION_CANCELLATION.equals(event)) {
      onUserAuthorizationCancellation(message);
    } else {
      logger.info("未处理事件类型 event={}", event);
    }
  }

  /**
   * 仅处理 SYSUSER / 微信同步可能出现的撤销项。
   */
  private void onUserAuthorizationRevoke(WxPushMessage message) {
    String openId = message.getOpenId();
    String revokeInfo = message.getRevokeInfo();
    if (!StringUtils.hasText(openId)) {
      logger.warn("撤回事件缺少 OpenID");
      return;
    }
    if (!StringUtils.hasText(revokeInfo)) {
      logger.warn("撤回事件缺少 RevokeInfo，openId={}", openId);
      return;
    }

    switch (revokeInfo) {
      case WxRevokeInfo.PHONE:
        // FacialService: sysUser.setMobile / setTel(phone)
        bizServerClient.notifyClear(
                openId, message.getEvent(), revokeInfo, BizClearAction.CLEAR_PHONE);
        break;
      case WxRevokeInfo.EMAIL:
        // SYSUSER.EMAIL；合规邮件常见项
        bizServerClient.notifyClear(
            openId, message.getEvent(), revokeInfo, BizClearAction.CLEAR_EMAIL);
        break;
      case WxRevokeInfo.NICKNAME_AVATAR:
      case WxRevokeInfo.WECHAT_NICKNAME:
      case WxRevokeInfo.WECHAT_AVATAR:
        // SYSUSER 无微信昵称/头像字段；displayName 是身份证姓名，禁止当微信资料清
        logger.info(
            "撤回昵称/头像 openId={}：SYSUSER 未同步微信昵称头像，忽略",
            openId);
        break;
      default:
        logger.info(
            "撤回 {}({}) openId={}：不在 SYSUSER 微信同步范围内，忽略",
            WxRevokeInfo.describe(revokeInfo),
            revokeInfo,
            openId);
        break;
    }
  }

  private void onUserAuthorizationCancellation(WxPushMessage message) {
    String openId = message.getOpenId();
    if (!StringUtils.hasText(openId)) {
      return;
    }
    // 注销：清相关个人信息并解绑 OPENID
    bizServerClient.notifyClear(
        openId, message.getEvent(), message.getRevokeInfo(), BizClearAction.CLEAR_ALL);
  }
}
