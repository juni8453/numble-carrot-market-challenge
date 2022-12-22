package com.market.carrot.security.login.repository;

import com.market.carrot.security.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByUsername(String username);
}
