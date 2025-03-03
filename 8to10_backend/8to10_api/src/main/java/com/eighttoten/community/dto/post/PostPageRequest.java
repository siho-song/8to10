package com.eighttoten.community.dto.post;

import com.eighttoten.community.domain.post.SearchPostPage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostPageRequest {
    private String keyword; //제목, 내용, 닉네임으로 검색
    private String searchCond; //TITLE,CONTENTS,WRITER
    private String sortCond;
    private String sortDirection;
    @NotNull
    @Min(value = 1)
    private Long pageNum;
    @NotNull
    @Min(value = 1)
    private Long pageSize;


    public SearchPostPage toSearchPostPage(){
        return new SearchPostPage(keyword, searchCond, sortCond, sortDirection, pageNum, pageSize);
    }
}