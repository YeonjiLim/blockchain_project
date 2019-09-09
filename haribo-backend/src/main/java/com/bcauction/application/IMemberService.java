package com.bcauction.application;

import com.bcauction.domain.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMemberService {
    List<Member> checkList();
    Member search(long id);
    Member search(String email);

    @Transactional
    Member add(Member member);

    @Transactional
    Member update(Member memeber);

    @Transactional
    void delete(long id);
}
