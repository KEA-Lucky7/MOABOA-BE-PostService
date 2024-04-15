package com.example.lucky7postservice.src.comment.api;

import com.example.lucky7postservice.src.comment.api.dto.PostCommentReq;
import com.example.lucky7postservice.src.comment.api.dto.PostCommentRes;
import com.example.lucky7postservice.src.comment.application.CommentService;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {
    private final CommentService commentService;

    /* 댓글 달기 API */
    @Operation(summary = "댓글 등록 API", description="글에 댓글을 답니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-ERR-005", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
            @ApiResponse(responseCode = "GLB-ERR-008", description = "존재하지 않는 글입니다.")
    })
    @PostMapping("/{postId}/comment")
    public BaseResponse<PostCommentRes> comment(@PathVariable Long postId, @RequestBody PostCommentReq postCommentReq) throws BaseException {
        return new BaseResponse<>(commentService.comment(postId, postCommentReq));
    }
}
