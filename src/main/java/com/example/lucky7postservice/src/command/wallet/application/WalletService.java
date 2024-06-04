package com.example.lucky7postservice.src.command.wallet.application;

import com.example.lucky7postservice.src.auth.AuthServiceClient;
import com.example.lucky7postservice.src.command.wallet.api.dto.ConsumedRes;
import com.example.lucky7postservice.src.command.wallet.api.dto.GetCalenderRes;
import com.example.lucky7postservice.src.query.repository.BlogQueryRepository;
import com.example.lucky7postservice.src.query.repository.MemberQueryRepository;
import com.example.lucky7postservice.src.query.repository.WalletQueryRepository;
import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import com.example.lucky7postservice.utils.config.SetTime;
import com.example.lucky7postservice.utils.entity.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {
    private final WalletQueryRepository walletQueryRepository;

    private final MemberQueryRepository memberQueryRepository;
    private final AuthServiceClient authServiceClient;
    private Long memberId = 1L;

    private final BlogQueryRepository blogQueryRepository;

    /* 캘린더 반환 */
    public GetCalenderRes getCalender(String month, String specificDate) throws BaseException {
        // 멤버 예외 처리
        memberId = userAndBlogValidation();

        LocalDate start = SetTime.getMonthStart(month);
        LocalDate end = SetTime.getMonthEnd(month);

        LocalDate today = LocalDate.now();

        LocalDate specific = SetTime.stringToLocalDate(specificDate);
        if(!SetTime.specificDateValidation(specific)) throw new BaseException(BaseResponseStatus.INVALID_DATE);

        Integer specificAmount = walletQueryRepository.findByMemberIdAndStateAndConsumedDate(memberId, specific, today)
                .orElse(0);

        LocalDate monthAgo = today.minusMonths(1);
        Integer monthAmount = walletQueryRepository.findByMemberIdAndStateAndConsumedDate(memberId, monthAgo, today)
                .orElse(0);

        Integer totalAmount = walletQueryRepository.findByMemberIdAndStateAndConsumedDate(memberId, start, end)
                .orElse(0);

        List<ConsumedRes> consumedList = walletQueryRepository.findAllByMemberIdAndStateAndConsumedDate(memberId, start, end);
        return new GetCalenderRes(memberId,
                specificAmount, monthAmount, totalAmount, consumedList);
    }

    /* 멤버, 블로그 예외 처리 */
    public Long userAndBlogValidation() throws BaseException {
        // 멤버 예외 처리
        memberId = authServiceClient.validateToken().getBody();
        memberQueryRepository.findByIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));

        // 블로그 예외 처리
        blogQueryRepository.findByMemberIdAndState(memberId, State.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_BLOG));

        return memberId;
    }
}
