package com.market.carrot.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalResponseMessage {

  SUCCESS_PRODUCT_LIKE("제품 좋아요 성공"),

  SUCCESS_GET_MEMBER("프로필 조회 성공"),

  SUCCESS_DELETE_MEMBER("프로필 삭제 성공"),

  SUCCESS_GET_PRODUCTS("모든 상품 조회 성공"),

  SUCCESS_GET_PRODUCT("단건 상품 조회 성공"),

  SUCCESS_POST_INSERT_PRODUCT("상품 등록 성공"),

  SUCCESS_POST_UPDATE_PRODUCT("상품 수정 성공"),

  SUCCESS_DELETE_PRODUCT("상품 삭제 성공"),

  SUCCESS_POST_UPDATE_IMAGE("이미지 수정 성공"),

  SUCCESS_DELETE_IMAGE("이미지 삭제 성공");

  private final String successMessage;
}
