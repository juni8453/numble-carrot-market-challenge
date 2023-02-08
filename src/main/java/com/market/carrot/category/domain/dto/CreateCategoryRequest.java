package com.market.carrot.category.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateCategoryRequest {

  @NotBlank(message = "카테고리 제목을 적어주세요.")
  private String name;

  private CreateCategoryRequest(String name) {
    this.name = name;
  }

  public static CreateCategoryRequest testConstructor(String name) {
    return new CreateCategoryRequest(name);
  }
}
