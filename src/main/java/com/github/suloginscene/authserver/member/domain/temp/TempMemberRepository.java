package com.github.suloginscene.authserver.member.domain.temp;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.profile.ProfileChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TempMemberRepository {

    private final TempMemberJpaRepository tempMemberJpaRepository;
    private final ProfileChecker profileChecker;


    public TempMember save(TempMember tempMember) {
        return tempMemberJpaRepository.save(tempMember);
    }

    public boolean existsByEmail(Email email) {
        return tempMemberJpaRepository.existsByEmail(email);
    }


    public void deleteAll() {
        profileChecker.checkTest();
        tempMemberJpaRepository.deleteAll();
    }

}
