package com.bcauction.api;

import com.bcauction.application.IMemberService;
import com.bcauction.domain.Member;
import com.bcauction.domain.exception.DomainException;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MemberController {
    public static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private IMemberService memberService;

    @Autowired
    public MemberController(IMemberService memberService) {
        Assert.notNull(memberService, "memberService 개체가 반드시 필요!");
        this.memberService = memberService;
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public List<Member> checkList() {
        List<Member> list = memberService.checkList();

        if (list == null || list.isEmpty() )
            throw new EmptyListException("NO DATA");

        return list;
    }

    @RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
    public Member search(@PathVariable int id) {

        Member member = memberService.search(id);
        if (member == null) {
            logger.error("NOT FOUND ID: ", id);
            throw new NotFoundException(id + " member 정보를 찾을 수 없습니다.");
        }

        return member;
    }

    @RequestMapping(value = "/members/login", method = RequestMethod.POST)
    public Member login(@RequestBody Member member) {
        Member mem = memberService.search(member.getEmail());
        if (!mem.getPassword().equals(member.getPassword()))
            throw new DomainException("비밀번호가 일치하지 않습니다.");
        return mem;
    }

    @RequestMapping(value = "/members", method = RequestMethod.POST)
    public Member add(@RequestBody Member member) {
        return memberService.add(member);
    }

    @RequestMapping(value = "/members", method = RequestMethod.PUT)
    public Member update(@RequestBody Member member) {
        return memberService.update(member);
    }

    @RequestMapping(value = "/members/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        memberService.delete(id);
    }

}
