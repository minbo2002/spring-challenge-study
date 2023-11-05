package com.example.springchallenge2week.common.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public abstract class PageRequestDto {  // 커스터마이징 페이징 요청 DTO

    @NotNull
    @Min(1)
    private int page;

    @NotNull
    @Min(1)
    private int size;

    public PageRequestDto() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(){
        return PageRequest.of(page -1, size);  // Spring Data Jpa에서는 page가 0부터 시작하므로 1을 빼줌
    }
}
