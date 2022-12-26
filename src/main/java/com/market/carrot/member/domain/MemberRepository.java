package com.market.carrot.member.domain;

import com.market.carrot.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
