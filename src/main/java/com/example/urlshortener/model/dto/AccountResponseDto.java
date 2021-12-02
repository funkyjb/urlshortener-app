package com.example.urlshortener.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountResponseDto {

    private boolean success;
    private String description;
    private String password;
}
