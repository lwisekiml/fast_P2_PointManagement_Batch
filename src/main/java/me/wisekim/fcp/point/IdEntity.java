package me.wisekim.fcp.point;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;


/*
모든 테이블에는 `id` bigint NOT NULL AUTO_INCREMENT 가 포함되어 있다.
따라서 아래와 같이 Id 생성 규칙을 일괄적으로 구현하고 모든 Entity가 IdEntity를 상속해서 Id를 따로 구현하지 않아도 되게 한다.
 */
@MappedSuperclass // 테이블이 없는 상위 클래스가 하위 entity 클래스에게 매핑정보를 제공할 수 있음
@Getter
public abstract class IdEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id 생성을 Database에 위임합니다. 즉, AUTO_INCREMENT로 Id를 생성합니다
    private Long id;
}
