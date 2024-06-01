package com.example.lucky7postservice.src.command.wallet.api;

import com.example.lucky7postservice.src.command.wallet.api.dto.GetCalenderRes;
import com.example.lucky7postservice.src.command.wallet.application.WalletService;
import com.example.lucky7postservice.utils.config.BaseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class WalletController {
    private final WalletService walletService;

    /* 캘린더 조회 API */
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
    @GetMapping("/calender")
    public ResponseEntity<GetCalenderRes> getCalender(
            @RequestParam("month") String month,
            @RequestParam("specificDate") String specificDate) throws BaseException {
        // TODO : Authorization에서 jwt 추출하기
        return new ResponseEntity<>(walletService.getCalender(month, specificDate),
                HttpStatus.OK);
    }
}
