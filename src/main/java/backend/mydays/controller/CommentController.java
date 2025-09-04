package backend.mydays.controller;

import backend.mydays.dto.comment.CommentCreateRequest;
import backend.mydays.dto.comment.CommentCreateResponse;
import backend.mydays.dto.comment.CommentUpdateRequest;
import backend.mydays.dto.common.BaseResponse;
import backend.mydays.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "댓글", description = "게시물 댓글 관련 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "특정 게시물에 댓글을 작성합니다.")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<BaseResponse<CommentCreateResponse>> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long commentId = commentService.createComment(postId, request, userDetails.getUsername());
        return BaseResponse.created("댓글이 작성되었습니다.", new CommentCreateResponse(commentId));
    }

    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        commentService.updateComment(commentId, request, userDetails.getUsername());
        return BaseResponse.ok("댓글이 수정되었습니다.", null);
    }

    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다.")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        commentService.deleteComment(commentId, userDetails.getUsername());
        return BaseResponse.ok("댓글이 삭제되었습니다.", null);
    }
}
