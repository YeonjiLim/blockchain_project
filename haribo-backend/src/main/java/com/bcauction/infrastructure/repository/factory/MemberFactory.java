package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberFactory {

    public static Member create(ResultSet rs) throws SQLException {
        if (rs == null) return null;
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPassword(rs.getString("password"));
        member.setRegistration_date(rs.getTimestamp("registration_date").toLocalDateTime());

        return member;
    }
}
