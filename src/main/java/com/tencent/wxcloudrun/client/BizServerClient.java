package com.tencent.wxcloudrun.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用 A104527 业务服务，按 openId 清理 SYSUSER 等数据。
 * 27 侧接口就绪后，只需改配置里的 URL，不必再改推送分支。
 */
@Component
public class BizServerClient {

  private final Logger logger = LoggerFactory.getLogger(BizServerClient.class);
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${biz.server.base-url:}")
  private String baseUrl;

  @Value("${biz.server.clear-path:/api/wx/authorization/clear}")
  private String clearPath;

  @Value("${biz.server.enabled:false}")
  private boolean enabled;

  /**
   * @param openId     微信 OpenID（对应 SYSUSER.OPENID）
   * @param event      微信 Event
   * @param revokeInfo RevokeInfo，注销/资料变更可为空
   * @param action     业务动作：CLEAR_PHONE / CLEAR_EMAIL / UNBIND_OPENID / CLEAR_ALL
   */
  public void notifyClear(String openId, String event, String revokeInfo, String action) {
    if (!enabled || !StringUtils.hasText(baseUrl)) {
      logger.warn(
          "业务清理接口未启用，仅记录。openId={}, event={}, revokeInfo={}, action={}",
          openId, event, revokeInfo, action);
      return;
    }

    String url = trimSlash(baseUrl) + clearPath;
    Map<String, Object> body = new HashMap<>();
    body.put("openId", openId);
    body.put("event", event);
    body.put("revokeInfo", revokeInfo);
    body.put("action", action);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    try {
      restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
      logger.info("已通知业务服务清理 openId={}, action={}", openId, action);
    } catch (Exception e) {
      logger.error("通知业务服务失败 openId={}, action={}, url={}", openId, action, url, e);
      throw e;
    }
  }

  private static String trimSlash(String url) {
    if (url.endsWith("/")) {
      return url.substring(0, url.length() - 1);
    }
    return url;
  }
}
