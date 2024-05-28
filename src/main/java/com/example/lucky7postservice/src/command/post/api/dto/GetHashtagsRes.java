package com.example.lucky7postservice.src.command.post.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetHashtagsRes {
    List<String> freeList;
    List<String> walletList;

    public GetHashtagsRes(List<String> freeList, List<String> walletList) {
        this.freeList = freeList;
        this.walletList = walletList;
    }
}
