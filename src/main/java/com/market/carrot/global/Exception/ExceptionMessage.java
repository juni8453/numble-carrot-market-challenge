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

  IS_NOT_WRITER_BY_UPDATE("수정 가능한 회원이 아닙니다."),

  IS_NOT_WRITER_BY_DELETE("삭제 가능한 회원이 아닙니다.");

  private final String errorMessage;
}
