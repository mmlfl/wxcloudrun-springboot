package com.tencent.wxcloudrun.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信云托管消息推送请求体（建议控制台选择 JSON 模式）。
 *
 * <p>事件类型在 {@link #event} 字段，模板计数器接口里本来就没有。</p>
 *
 * <h3>配置探测</h3>
 * <pre>{ "action": "CheckContainerPath" }</pre>
 *
 * <h3>用户撤回授权示例</h3>
 * <pre>
 * {
 *   "ToUserName": "gh_xxx",
 *   "FromUserName": "平台推送服务UserName",
 *   "CreateTime": 1627359464,
 *   "MsgType": "event",
 *   "Event": "user_authorization_revoke",
 *   "OpenID": "oaKk343WOktAaT2ygsX138BGblrg",
 *   "AppID": "wx13974bf780d3dc89",
 *   "RevokeInfo": "16"
 * }
 * </pre>
 *
 * <p>清理用户请用 {@link #openId}，不要用 FromUserName。</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxPushMessage {

  /** 配置探测：CheckContainerPath */
  private String action;

  @JsonProperty("ToUserName")
  private String toUserName;

  @JsonProperty("FromUserName")
  private String fromUserName;

  @JsonProperty("CreateTime")
  private Long createTime;

  @JsonProperty("MsgType")
  private String msgType;

  /**
   * user_info_modified / user_authorization_revoke / user_authorization_cancellation
   */
  @JsonProperty("Event")
  private String event;

  /** 授权用户 OpenID */
  @JsonProperty("OpenID")
  private String openId;

  @JsonProperty("AppID")
  private String appId;

  /** 撤回项，16=邮箱 */
  @JsonProperty("RevokeInfo")
  private String revokeInfo;

  @JsonProperty("PluginID")
  private String pluginId;

  @JsonProperty("OpenPID")
  private String openPid;

  @JsonProperty("Content")
  private String content;

  @JsonProperty("MsgId")
  private Long msgId;
}
