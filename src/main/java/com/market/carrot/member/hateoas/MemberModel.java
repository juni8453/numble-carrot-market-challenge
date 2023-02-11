package com.market.carrot.member.hateoas;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.market.carrot.member.dto.response.MemberResponse;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;

@Getter
public class MemberModel extends EntityModel<MemberResponse> {

  @JsonUnwrapped
  private final MemberResponse member;

  public MemberModel(MemberResponse member) {
    this.member = member;
  }
}
