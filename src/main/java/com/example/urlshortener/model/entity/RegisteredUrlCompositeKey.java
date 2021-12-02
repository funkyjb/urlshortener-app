package com.example.urlshortener.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisteredUrlCompositeKey implements Serializable {
    private String originUrl;
    private String account;
}
