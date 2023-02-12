package com.market.carrot.global.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

  BAD_CREDENTIALS("일치하지 않는 비밀번호입니다."),

  NOT_FOUND_PRODUCT("존재하지 않는 상품입니다."),

  NOT_FOUND_MEMBER("존재하지 않는 회원입니다."),

  NOT_FOUND_IMAGE("존재하지 않는 이미지입니다."),

  NOT_FOUND_CATEGORY("존재하지 않는 카테고리입니다."),

  USER_DUPLICATED("중복되는 회원 아이디입니다."),

  IS_NOT_MY_PROFILE("조회할 수 있는 프로필이 아닙니다."),

  IS_NOT_MY_PROFILE_BY_DELETE("삭제할 수 있는 프로필이 아닙니다."),

  IS_NOT_INCLUDED_IMAGE("이미지를 하나 이상 등록해야합니다."),

  IS_NOT_WRITER("상품을 작성한 회원이 아닙니다.");

  private final String errorMessage;
}
