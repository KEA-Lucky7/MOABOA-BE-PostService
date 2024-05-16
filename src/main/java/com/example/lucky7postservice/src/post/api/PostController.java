package com.example.lucky7postservice.src.post.api;

import com.example.lucky7postservice.src.post.api.dto.*;
import com.example.lucky7postservice.src.post.application.PostService;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    /* 홈 화면 글 목록 조회 (좋아요순) API */
    @Operation(summary = "홈 화면 글 목록 조회 (좋아요순) API", description="홈 화면의 글(BEST) 목록을 반환합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-SUC-000", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다.")
    })
    @GetMapping("/home-post-list")
    public BaseResponse<List<GetHomePostsRes>> getHomePosts() throws BaseException {
        // TODO : Authorization에서 jwt 추출하기

        return new BaseResponse<>(postService.getHomePosts());
    }

    /* 글 작성하기 API */
    @Operation(summary = "글 작성 API", description="글을 작성합니다, 임시 저장 없이 글을 바로 작성하는 경우에는 0을 보내주세요")
    @Parameters({
            @Parameter(name = "Authorization", required = true, in = ParameterIn.HEADER, description = "Bearer 과 함께 보내주세요", schema = @Schema(type = "string"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-SUC-000", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다."),
    })
    @PostMapping("/{postId}")
    public BaseResponse<PostPostRes> postPost(@PathVariable Long postId, @Valid @RequestBody PostPostReq postReq) throws BaseException {
        // TODO : Authorization에서 jwt 추출하기

        return new BaseResponse<>(postService.postPost(postId, postReq));
    }

    /* 글 임시 저장하기 API */
    @Operation(summary = "글 임시 저장 API", description="글을 임시 저장합니다, 첫 임시저장이라면 postId에 0을 보내주세요")
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
    @PostMapping("/temp/{postId}")
    public BaseResponse<PostPostRes> savePost(@PathVariable Long postId, @Valid @RequestBody PostSavedPostReq postReq) throws BaseException {
        // TODO : Authorization에서 jwt 추출하기

        return new BaseResponse<>(postService.savePost(postId, postReq));
    }

    /* 글 임시 저장 목록 조회 API */
    @Operation(summary = "글 임시 저장 목록 조회 API", description="임시 저장한 글 목록을 반환합니다")
    @Parameters({
            @Parameter(name = "Authorization", required = true, in = ParameterIn.HEADER, description = "Bearer 과 함께 보내주세요", schema = @Schema(type = "string"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "GLB-SUC-000", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-001", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "GLB-ERR-005", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "GLB-ERR-006", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "GLB-ERR-007", description = "존재하지 않는 블로그입니다.")
    })
    @GetMapping("/temp-list")
    public BaseResponse<List<GetSavedPostsRes>> getSavedPosts() throws BaseException {
        // TODO : Authorization에서 jwt 추출하기

        return new BaseResponse<>(postService.getSavedPosts());
    }

    /* 글 삭제하기 API */
    @Operation(summary = "글 삭제 API", description="글을 삭제합니다")
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
    @DeleteMapping("/{postId}")
    public BaseResponse<String> deletePost(@PathVariable Long postId) throws BaseException {
        // TODO : Authorization에서 jwt 추출하기

        return new BaseResponse<>(postService.deletePost(postId));
    }

    /* 글 수정하기 API */
    @Operation(summary = "글 수정 API", description="글을 수정합니다")
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
    @PatchMapping("/{postId}")
    public BaseResponse<PostPostRes> modifyPost(@PathVariable Long postId, @Valid @RequestBody PostPostReq patchReq) throws BaseException {
        // TODO : Authorization에서 jwt 추출하기

        return new BaseResponse<>(postService.modifyPost(postId, patchReq));
    }
}
