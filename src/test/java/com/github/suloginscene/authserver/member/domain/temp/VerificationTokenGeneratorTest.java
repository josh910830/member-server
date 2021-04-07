package com.github.suloginscene.authserver.member.domain.temp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("인증 토큰 생성기")
class VerificationTokenGeneratorTest {

    @Test
    @DisplayName("생성 - 6자리 랜덤 문자열")
    void generate_onSuccess_createsRandomStringOf6() {
        String t1 = VerificationTokenGenerator.generate();
        String t2 = VerificationTokenGenerator.generate();

        assertThat(t1.length()).isEqualTo(6);
        assertThat(t2.length()).isEqualTo(6);
        assertThat(t1).isNotEqualTo(t2);
    }

}
