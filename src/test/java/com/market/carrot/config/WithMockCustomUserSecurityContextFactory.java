package com.market.carrot.config;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Member;
import com.market.carrot.login.domain.Role;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority("USER"));

    Member member = Member.testConstructor(
        1L, annotation.username(), "password", null, Role.USER
    );

    MemberContext memberContext = new MemberContext(member);

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        memberContext, memberContext.getPassword(), memberContext.getAuthorities());

    /**
     * Details 를 셋팅하지 않으면 Authentication 객체 내 Details 는 null 로 셋팅된다.
     * Details 값이 필요없으면 굳이 셋팅할 필요는 없다.
     */
    authentication.setDetails(memberContext);
    context.setAuthentication(authentication);

    return context;
  }
}
