package com.market.carrot.login.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<User, Long> {
}
