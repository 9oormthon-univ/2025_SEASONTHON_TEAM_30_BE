package backend.mydays.controller;

import backend.mydays.dto.comment.CommentCreateRequest;
import backend.mydays.dto.comment.CommentCreateResponse;
import backend.mydays.dto.comment.CommentUpdateRequest;
import backend.mydays.dto.common.BaseResponse;
import backend.mydays.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<BaseResponse<CommentCreateResponse>> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long commentId = commentService.createComment(postId, request, userDetails.getUsername());
        return BaseResponse.created("댓글이 작성되었습니다.", new CommentCreateResponse(commentId));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        commentService.updateComment(commentId, request, userDetails.getUsername());
        return BaseResponse.ok("댓글이 수정되었습니다.", null);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        commentService.deleteComment(commentId, userDetails.getUsername());
        return BaseResponse.ok("댓글이 삭제되었습니다.", null);
    }
}
