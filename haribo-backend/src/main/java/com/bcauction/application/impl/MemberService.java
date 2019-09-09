package com.bcauction.application.impl;

import com.bcauction.application.IMemberService;
import com.bcauction.domain.Member;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.repository.IMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService implements IMemberService {

    private IMemberRepository memberRepository;

    @Autowired
    public MemberService(IMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public List<Member> checkList() {
        return this.memberRepository.checkList();
    }

    @Override
    public Member search(long id) {
        return this.memberRepository.search(id);
    }

    @Override
    public Member search(String email) { return this.memberRepository.search(email); }

    @Override
    public Member add(Member member) {
        long id = this.memberRepository.add(member);
        return this.memberRepository.search(id);
    }

    @Override
    public Member update(Member member) {

        Member found = this.memberRepository.search(member.getEmail());
        if(found == null)
            throw new ApplicationException("member 정보를 찾을 수 없습니다.");

        if(member.getId() == 0)
            member.setId(found.getId());
        if(member.getName() == null)
            member.setName(found.getName());
        if(member.getPassword() == null)
            member.setPassword(found.getPassword());

        int affected = this.memberRepository.update(member);
        if(affected == 0)
            throw new ApplicationException("작품정보수정 처리가 반영되지 않았습니다.");

        return this.memberRepository.search(member.getId());
    }

    @Override
    public void delete(long id) {
        this.memberRepository.delete(id);
    }
}
