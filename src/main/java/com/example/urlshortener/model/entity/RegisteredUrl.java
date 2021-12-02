package com.example.urlshortener.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RegisteredUrlCompositeKey.class)
public class RegisteredUrl {

    @Id
    private String originUrl;
    @Id
    @ManyToOne
    private Account account;
    private String shortenUrlId;
    private int redirectType;
    private int callsCount;
}
