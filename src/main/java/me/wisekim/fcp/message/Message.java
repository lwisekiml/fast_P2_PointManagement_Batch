package me.wisekim.fcp.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.wisekim.fcp.point.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Message extends IdEntity {
    // user 식별자
    @Column(name = "user_id", unique = true, nullable = false)
    String userId;
    // 메시지 제목
    @Column(name = "title", nullable = false)
    String title;
    // 메시지 내용
    @Column(name = "content", nullable = false, columnDefinition = "text")
    String content;
}