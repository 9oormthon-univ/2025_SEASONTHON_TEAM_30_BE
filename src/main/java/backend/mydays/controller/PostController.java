package backend.mydays.controller;

import backend.mydays.dto.common.BaseResponse;
import backend.mydays.dto.common.PageResponseDto;
import backend.mydays.dto.post.*;
import backend.mydays.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "게시물", description = "게시물 관련 API")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 생성", description = "오늘의 챌린지에 맞는 사진과 글을 업로드하여 게시물을 작성합니다.")
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
        return BaseResponse.created("게시물이 성공적으로 작성되었습니다.", new PostCreateResponse(String.valueOf(postId)));
    }

    @Operation(summary = "피드(게시물 목록) 조회", description = "다른 사람들이 올린 챌린지 게시물들을 최신순으로 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<PageResponseDto<FeedPostDto>>> getFeed(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PageResponseDto<FeedPostDto> response = new PageResponseDto<>(postService.getFeed(pageable, userDetails.getUsername()));
        return BaseResponse.ok("피드 조회에 성공했습니다.", response);
    }

    @Operation(summary = "게시물 작성화면 조회", description = "게시물 작성 화면에 필요한 오늘의 미션 내용을 조회합니다.")
    @GetMapping("/mission")
    public ResponseEntity<BaseResponse<MissionTextDto>> getMissionText() {
        MissionTextDto response = postService.getMissionText();
        return BaseResponse.ok("오늘의 미션 조회에 성공했습니다.", response);
    }

    @Operation(summary = "게시물 상세 조회", description = "특정 게시물의 상세 내용과 댓글들을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostDetailResponseWrapperDto>> getPostDetail(
            @PathVariable String postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PostDetailResponseWrapperDto response = postService.getPostDetail(Long.parseLong(postId), userDetails.getUsername());
        return BaseResponse.ok("게시물 상세 조회에 성공했습니다.", response);
    }

    @Operation(summary = "게시물 삭제", description = "내가 작성한 게시물을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> deletePost(
            @RequestBody PostIdRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.deletePost(Long.parseLong(request.getPostId()), userDetails.getUsername());
        return BaseResponse.ok("게시물이 삭제되었습니다.", null);
    }

    @Operation(summary = "게시물 좋아요", description = "다른 사람의 게시물에 '좋아요'를 누릅니다.")
    @PostMapping("/like")
    public ResponseEntity<BaseResponse<Void>> likePost(
            @RequestBody PostIdRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.likePost(Long.parseLong(request.getPostId()), userDetails.getUsername());
        return BaseResponse.ok("게시물을 좋아합니다.", null);
    }

    @Operation(summary = "게시물 좋아요 취소", description = "다른 사람의 게시물에 '좋아요'를 취소합니다.")
    @PostMapping("/unlike")
    public ResponseEntity<BaseResponse<Void>> unlikePost(
            @RequestBody PostIdRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.unlikePost(Long.parseLong(request.getPostId()), userDetails.getUsername());
        return BaseResponse.ok("게시물 좋아요를 취소했습니다.", null);
    }
}
