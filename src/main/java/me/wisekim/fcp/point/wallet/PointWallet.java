package me.wisekim.fcp.point.wallet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.wisekim.fcp.point.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table // 이 클래스에 맞는 테이블을 가지고 있습니다. 그 이름은 point_wallet 이다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // proctected인 기본 생성자를 자동으로 생성
@AllArgsConstructor // 모든 인자를 다 받는 생성자를 public으로 자동으로 생성
@Getter
public class PointWallet extends IdEntity {
    @Column(name = "user_id", unique = true, nullable = false) // user_id라는 이름의 컬럼과 매핑. unique하고 null이면 안됨
    String userId;
    @Column(name = "amount", columnDefinition = "BIGINT") // amount라는 이름의 컬럼과 매핑합니다. 그 타입은 BIGINT 이다.
    BigInteger amount;
}
