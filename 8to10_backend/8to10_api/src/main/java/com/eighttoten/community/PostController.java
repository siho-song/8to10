package com.eighttoten.community;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.common.Pagination;
import com.eighttoten.community.domain.post.PostPreview;
import com.eighttoten.community.dto.post.PostDetailResponse;
import com.eighttoten.community.dto.post.PostPageRequest;
import com.eighttoten.community.dto.post.PostPreviewResponse;
import com.eighttoten.community.dto.post.PostSaveRequest;
import com.eighttoten.community.dto.post.PostUpdateRequest;
import com.eighttoten.community.service.PostHeartService;
import com.eighttoten.community.service.PostScrapService;
import com.eighttoten.community.service.PostService;
import com.eighttoten.member.domain.Member;
import com.eighttoten.support.CurrentMember;
import com.eighttoten.support.PageResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostHeartService postHeartService;
    private final PostScrapService postScrapService;

    @GetMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<PostPreviewResponse>> getPostPage(@Valid PostPageRequest request)
    {
        Pagination<PostPreview> postPreviews = postService.searchPostPreviewPages(request.toSearchPostPage());
        List<PostPreviewResponse> postPreviewResponses = postPreviews.getContents().stream().map(PostPreviewResponse::from).toList();
        PageResponse<PostPreviewResponse> response = new PageResponse<>(postPreviewResponses,
                postPreviews.getPageNumber(), postPreviews.getPageSize(),
                postPreviews.getTotalPages(), postPreviews.getTotalElements());

        return new ResponseEntity<>(response, OK);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePost(
            @CurrentMember Member member,
            @Valid @RequestBody PostUpdateRequest request) {

        postService.updatePost(member, request.toUpdatePost());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDetailResponse> getPostDetail(
            @CurrentMember Member member,
            @PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(PostDetailResponse.from(postService.searchPostDetailInfo(member.getId(), id)), OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePostById(
            @CurrentMember Member member,
            @PathVariable(name = "id") Long id) {

        postService.deletePost(member, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/save", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> save(
            @CurrentMember Member member,
            @RequestBody @Valid PostSaveRequest request) {

        postService.addPost(request.toNewPost(member.getId()));
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping(value = "/{id}/heart", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveHeart(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long postId) {

        postHeartService.save(member, postId);
        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping(value = "/{id}/heart", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteHeartByPostId(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long postId) {

        postHeartService.deleteByMemberIdAndPostId(member.getId(), postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/scrap", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveScrap(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long postId) {

        postScrapService.save(member, postId);
        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping(value = "/{id}/scrap", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteScrapByPostId(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long postId) {

        postScrapService.deleteByMemberIdAndPostId(member.getId(), postId);
        return ResponseEntity.noContent().build();
    }
}