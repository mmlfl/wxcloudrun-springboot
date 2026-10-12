package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.WxPushMessage;

public interface WxPushService {

  /**
   * 处理推送。无论业务成败，上层都应尽快向微信返回 success，避免重试刷屏。
   */
  void handle(WxPushMessage message);
}
