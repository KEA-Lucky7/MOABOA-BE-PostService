package com.example.lucky7postservice.src.post.api;

import com.example.lucky7postservice.src.post.api.dto.PostPostReq;
import com.example.lucky7postservice.src.post.api.dto.PostPostRes;
import com.example.lucky7postservice.src.post.api.dto.SavePostReq;
import com.example.lucky7postservice.src.post.application.PostService;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    /* 글 작성하기 API */
    @Operation(summary = "글 작성 API", description="글을 작성합니다, 임시 저장 없이 글을 바로 작성하는 경우에는 0을 보내주세요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-ERR-005", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
    })
    @PostMapping("/{postId}")
    public BaseResponse<PostPostRes> postPost(@PathVariable Long postId, @Valid @RequestBody PostPostReq postReq) throws BaseException {
        return new BaseResponse<>(postService.postPost(postId, postReq));
    }

    /* 글 임시 저장하기 API */
    @Operation(summary = "글 임시 저장 API", description="글을 임시 저장합니다, 첫 임시저장이라면 postId에 0을 보내주세요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-ERR-005", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
            @ApiResponse(responseCode = "GLB-ERR-008", description = "존재하지 않는 글입니다.")
            })
    @PostMapping("/{postId}/temporary")
    public BaseResponse<PostPostRes> savePost(@PathVariable Long postId, @Valid @RequestBody SavePostReq postReq) throws BaseException {
        return new BaseResponse<>(postService.savePost(postId, postReq));
    }

    /* 글 삭제하기 API */
    @Operation(summary = "글 삭제 API", description="글을 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-ERR-005", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
            @ApiResponse(responseCode = "GLB-ERR-008", description = "존재하지 않는 글입니다.")
    })
    @DeleteMapping("/{postId}")
    public BaseResponse<String> deletePost(@PathVariable Long postId) throws BaseException {
        return new BaseResponse<>(postService.deletePost(postId));
    }

    /* 글 수정하기 API */
    @Operation(summary = "글 수정 API", description="글을 수정합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-ERR-005", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
            @ApiResponse(responseCode = "GLB-ERR-008", description = "존재하지 않는 글입니다.")
    })
    @PatchMapping("/{postId}")
    public BaseResponse<PostPostRes> modifyPost(@PathVariable Long postId, @Valid @RequestBody PostPostReq patchReq) throws BaseException {
        return new BaseResponse<>(postService.modifyPost(postId, patchReq));
    }
}
