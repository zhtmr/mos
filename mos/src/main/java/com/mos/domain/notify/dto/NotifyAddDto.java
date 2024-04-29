package com.mos.domain.notify.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class NotifyAddDto {
  private String message;
  private int recipientId;
  private String link;
}
