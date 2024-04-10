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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    /* 중복 체크 API */
    @Operation(summary = "닉네임 중복 체크", description="닉네임 중복체크를 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "400", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "400", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "4001", description = "중복된 이름입니다.")
    })
    @PostMapping("/{memberId}")
    public BaseResponse<PostPostRes> postPost(@PathVariable Long memberId, @Valid @RequestBody PostPostReq postReq) throws BaseException {
        return new BaseResponse<>(postService.postPost(memberId, postReq));
    }
}
