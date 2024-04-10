package com.example.lucky7postservice.src.post.api;

import com.example.lucky7postservice.src.post.api.dto.PostPostReq;
import com.example.lucky7postservice.src.post.api.dto.PostPostRes;
import com.example.lucky7postservice.src.post.application.PostService;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    /* 글 작성하기 API */
    @Operation(summary = "글 작성 API", description="글을 작성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-ERR-005", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
    })
    @PostMapping("/{memberId}")
    public BaseResponse<PostPostRes> postPost(@PathVariable Long memberId, @Valid @RequestBody PostPostReq postReq) throws BaseException {
        return new BaseResponse<>(postService.postPost(memberId, postReq));
    }
}
