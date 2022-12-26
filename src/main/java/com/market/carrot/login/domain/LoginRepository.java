package com.market.carrot.login.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByUsername(String username);
}
