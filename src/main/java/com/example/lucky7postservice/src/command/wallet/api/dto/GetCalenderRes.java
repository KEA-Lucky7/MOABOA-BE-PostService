package com.example.lucky7postservice.src.command.wallet.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetCalenderRes {
    private Long memberId;
    private int specificAmount;
    private int monthAmount;
    private int totalAmount;
    private List<ConsumedRes> consumedList;

    public GetCalenderRes(Long memberId,
                          int specificAmount, int monthAmount, int totalAmount,
                          List<ConsumedRes> consumedList) {
        this.memberId = memberId;
        this.specificAmount = specificAmount;
        this.monthAmount = monthAmount;
        this.totalAmount = totalAmount;
        this.consumedList = consumedList;
    }
}
