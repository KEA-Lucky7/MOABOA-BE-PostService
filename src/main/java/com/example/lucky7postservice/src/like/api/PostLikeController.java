package com.example.lucky7postservice.src.like.api;

import com.example.lucky7postservice.src.like.application.PostLikeService;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostLikeController {
    private final PostLikeService postLikeService;

    /* 글 공감하기 API */
    @Operation(summary = "글 공감 API", description="글에 좋아요를 누릅니다")
    @Parameters({
            @Parameter(name = "Authorization", required = true, in = ParameterIn.HEADER, description = "Bearer 과 함께 보내주세요", schema = @Schema(type = "string"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-SUC-000", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
            @ApiResponse(responseCode = "GLB-ERR-008", description = "존재하지 않는 글입니다.")
    })
    @PostMapping("/{postId}/like")
    public BaseResponse<String> like(@PathVariable Long postId) throws BaseException {
        return new BaseResponse<>(postLikeService.like(postId));
    }

    @Operation(summary = "글 공감 취소 API", description="글 좋아요를 취소합니다")
    @Parameters({
            @Parameter(name = "Authorization", required = true, in = ParameterIn.HEADER, description = "Bearer 과 함께 보내주세요", schema = @Schema(type = "string"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-SUC-000", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
            @ApiResponse(responseCode = "GLB-ERR-008", description = "존재하지 않는 글입니다."),
            @ApiResponse(responseCode = "GLB-ERR-009", description = "존재하지 않는 글 좋아요입니다.")
    })
    @DeleteMapping("/{postId}/like")
    public BaseResponse<String> dislike(@PathVariable Long postId) throws BaseException {
        return new BaseResponse<>(postLikeService.dislike(postId));
    }
}
