package me.wisekim.fcp.point;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class ExpiredPointSummary {
    String userId; // 유저 ID
    BigInteger amount; // 만료 금액
}
