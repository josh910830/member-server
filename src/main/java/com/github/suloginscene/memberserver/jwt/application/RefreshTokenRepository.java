package com.github.suloginscene.memberserver.jwt.application;

import com.github.suloginscene.memberserver.jwt.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByValue(String value);

    @Modifying
    @Query("delete from RefreshToken as t where t in :tokens")
    void deleteInBatch(@Param("tokens") List<RefreshToken> tokens);

}
