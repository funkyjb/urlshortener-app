package com.example.urlshortener.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {

    @JsonProperty(value = "URL")
    private String url;
    private int redirectType;
}
