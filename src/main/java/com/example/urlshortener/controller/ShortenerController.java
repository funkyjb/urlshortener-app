package com.example.urlshortener.controller;

import com.example.urlshortener.model.dto.AccountRequestDto;
import com.example.urlshortener.model.dto.AccountResponseDto;
import com.example.urlshortener.model.dto.RegisterRequestDto;
import com.example.urlshortener.model.dto.RegisterResponseDto;
import com.example.urlshortener.model.entity.RegisteredUrl;
import com.example.urlshortener.service.ShortenerService;
import com.example.urlshortener.service.utils.ShortenerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.AbstractMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShortenerController {

    private final ShortenerService shortenerService;

    @PostMapping("/account")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto request) {
        var response = shortenerService.createAccount(request.getAccountId());
        return ResponseEntity
                .status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUrl(@RequestBody RegisterRequestDto request, Principal principal) {
        var response = shortenerService.registerUrl(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/statistic/{accountId}")
    public ResponseEntity<List<AbstractMap.SimpleImmutableEntry<String, Integer>>> statistic(@PathVariable String accountId) {
        return ResponseEntity.ok().body(shortenerService.getStatistic(accountId));
    }

    @GetMapping("/{urlId}")
    public ResponseEntity<?> redirectToOriginUrl(@PathVariable String urlId) throws URISyntaxException {
        RegisteredUrl registeredUrl = shortenerService.getRegisteredUrl(urlId);
        if (registeredUrl == null) {
            return ResponseEntity.notFound().build();
        }
        URI originUrl = new URI(registeredUrl.getOriginUrl());
        HttpStatus redirectStatus = HttpStatus.valueOf(registeredUrl.getRedirectType());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(originUrl);
        return new ResponseEntity<>(httpHeaders, redirectStatus);
    }
}
