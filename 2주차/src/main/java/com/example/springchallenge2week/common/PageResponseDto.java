package com.example.springchallenge2week.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@Getter
@Builder
@ToString
public class PageResponseDto<T> {  // 커스터마이징 페이징 응답 DTO

    // 페이징 content
    private List<T> content;

    //현재 페이지 번호
    private int page;
    //목록 사이즈
    private int size;
    // 게시글 총갯수
    private long totalCount;
    //시작 페이지 번호, 끝 페이지 번호
    private int start, end;
    //이전, 다음
    private boolean prev, next;

    public static <T> PageResponseDto<T> of(Page<T> pageInfo) {
        Pageable pageable = pageInfo.getPageable();
        int totalPage = pageInfo.getTotalPages();

        int page = pageable.getPageNumber() + 1;   // 0부터 시작하므로 1을 추가
        int size = pageable.getPageSize();

        int tempEnd = (int) (Math.ceil(page / (double) size)) * size;

        int start = tempEnd - (size - 1);
        int end = Math.min(totalPage, tempEnd);
        boolean prev = start > 1;
        boolean next = totalPage > tempEnd;

        return PageResponseDto.<T>builder()
                .content(pageInfo.getContent())
                .page(page)
                .size(size)
                .totalCount(pageInfo.getTotalElements())
                .start(start)
                .end(end)
                .prev(prev)
                .next(next)
                .build();
    }
}
