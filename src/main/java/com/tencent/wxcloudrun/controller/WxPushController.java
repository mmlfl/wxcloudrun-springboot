package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.WxPushMessage;
import com.tencent.wxcloudrun.service.WxPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信云托管消息推送入口。
 *
 * <p>控制台配置：设置 → 消息推送 → 服务选本服务 → path 填 {@code /wx/push} → 模式选 JSON。</p>
 * <p>必须返回纯文本 {@code success}（或空串），HTTP 200，否则配置探测失败 / 微信会重试。</p>
 */
@RestController
public class WxPushController {

  private final WxPushService wxPushService;
  private final Logger logger = LoggerFactory.getLogger(WxPushController.class);

  public WxPushController(@Autowired WxPushService wxPushService) {
    this.wxPushService = wxPushService;
  }

  @PostMapping(value = "/wx/push", produces = MediaType.TEXT_PLAIN_VALUE)
  public String receive(
      @RequestBody(required = false) WxPushMessage message,
      @RequestHeader(value = "x-wx-source", required = false) String wxSource) {

    // 若开启了公网访问，可用该头判断是否来自微信侧（内网推送一般也会带）
    if (StringUtils.hasText(wxSource)) {
      logger.debug("x-wx-source={}", wxSource);
    }

    try {
      logger.info(
          "消息推送入参 action={}, msgType={}, event={}, openId={}, revokeInfo={}",
          message == null ? null : message.getAction(),
          message == null ? null : message.getMsgType(),
          message == null ? null : message.getEvent(),
          message == null ? null : message.getOpenId(),
          message == null ? null : message.getRevokeInfo());
      wxPushService.handle(message);
    } catch (Exception e) {
      // 业务异常也回 success，避免微信疯狂重试；细节看日志排查
      logger.error("处理消息推送异常，仍返回 success", e);
    }

    return "success";
  }
}
