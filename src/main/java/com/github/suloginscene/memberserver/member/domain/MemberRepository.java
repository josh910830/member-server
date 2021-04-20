package com.github.suloginscene.memberserver.member.domain;

import com.github.suloginscene.exception.NotFoundException;
import com.github.suloginscene.profile.ProfileChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final ProfileChecker profileChecker;


    public Member findById(Long id) {
        return memberJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Member.class, id));
    }

    public Member findByEmail(Email email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Member.class, email));
    }

    public void save(Member member) {
        memberJpaRepository.save(member);
    }

    public boolean existsByEmail(Email email) {
        return memberJpaRepository.existsByEmail(email);
    }

    public void deleteById(Long id) {
        memberJpaRepository.deleteById(id);
    }

    public void deleteAll() {
        profileChecker.checkTest();
        memberJpaRepository.deleteAll();
    }

}
