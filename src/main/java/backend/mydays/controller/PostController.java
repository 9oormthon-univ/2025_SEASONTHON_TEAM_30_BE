package backend.mydays.controller;

import backend.mydays.dto.common.BaseResponse;
import backend.mydays.dto.common.PageResponseDto;
import backend.mydays.dto.post.PostCreateRequest;
import backend.mydays.dto.post.PostCreateResponse;
import backend.mydays.dto.post.PostDetailResponseDto;
import backend.mydays.dto.post.PostResponseDto;
import backend.mydays.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<BaseResponse<PostCreateResponse>> createPost(
            @RequestPart("content") PostCreateRequest request,
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // In a real application, the image would be uploaded to a cloud storage (e.g., S3) and its URL would be passed to the service.
        // For now, we'll use a placeholder URL.
        String imageUrl = "https://example.com/images/" + image.getOriginalFilename(); // Placeholder
        Long postId = postService.createPost(request, userDetails.getUsername(), imageUrl);
        return BaseResponse.created("게시물이 성공적으로 작성되었습니다.", new PostCreateResponse(postId));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponseDto<PostResponseDto>>> getFeed(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PageResponseDto<PostResponseDto> response = new PageResponseDto<>(postService.getFeed(pageable, userDetails.getUsername()));
        return BaseResponse.ok("피드 조회에 성공했습니다.", response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostDetailResponseDto>> getPostDetail(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PostDetailResponseDto response = postService.getPostDetail(postId, userDetails.getUsername());
        return BaseResponse.ok("게시물 상세 조회에 성공했습니다.", response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.deletePost(postId, userDetails.getUsername());
        return BaseResponse.ok("게시물이 삭제되었습니다.", null);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<BaseResponse<Void>> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.likePost(postId, userDetails.getUsername());
        return BaseResponse.ok("게시물을 좋아합니다.", null);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<BaseResponse<Void>> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.unlikePost(postId, userDetails.getUsername());
        return BaseResponse.ok("게시물 좋아요를 취소했습니다.", null);
    }
}
