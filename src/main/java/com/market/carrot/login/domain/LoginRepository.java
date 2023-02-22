package com.market.carrot.login.domain;

import com.market.carrot.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByUsername(String username);
}
