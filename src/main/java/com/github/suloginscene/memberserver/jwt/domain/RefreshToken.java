package com.github.suloginscene.memberserver.jwt.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class RefreshToken {

    @Id @GeneratedValue
    private Long id;

    @Getter
    private Long memberId;

    @Getter
    private String value;

    private LocalDateTime expiredAt;


    private RefreshToken(Long memberId, String value, LocalDateTime expiredAt) {
        this.memberId = memberId;
        this.value = value;
        this.expiredAt = expiredAt;
    }

    public static RefreshToken of(Long memberId, int expDays) {
        String value = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(expDays);
        return new RefreshToken(memberId, value, expiredAt);
    }


    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }

    public void extendExpiration(int expDays) {
        expiredAt = LocalDateTime.now().plusDays(expDays);
    }

}
