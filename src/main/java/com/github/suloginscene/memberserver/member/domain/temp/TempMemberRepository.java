package com.github.suloginscene.memberserver.member.domain.temp;

import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.exception.NotFoundException;
import com.github.suloginscene.profile.ProfileChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TempMemberRepository {

    private final TempMemberJpaRepository tempMemberJpaRepository;
    private final ProfileChecker profileChecker;


    public boolean existsByEmail(Email email) {
        return tempMemberJpaRepository.existsByEmail(email);
    }

    public TempMember save(TempMember tempMember) {
        return tempMemberJpaRepository.save(tempMember);
    }

    public TempMember findById(Long id) {
        return tempMemberJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TempMember.class, id));
    }

    public void delete(TempMember tempMember) {
        tempMemberJpaRepository.delete(tempMember);
    }

    public void deleteAll() {
        profileChecker.checkTest();
        tempMemberJpaRepository.deleteAll();
    }

}
