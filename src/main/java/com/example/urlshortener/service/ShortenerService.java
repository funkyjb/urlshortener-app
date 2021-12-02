package com.example.urlshortener.service;

import com.example.urlshortener.model.dto.AccountResponseDto;
import com.example.urlshortener.model.dto.RegisterRequestDto;
import com.example.urlshortener.model.dto.RegisterResponseDto;
import com.example.urlshortener.model.entity.Account;
import com.example.urlshortener.model.entity.RegisteredUrl;
import com.example.urlshortener.repository.AccountRepository;
import com.example.urlshortener.repository.RegisteredUrlRepository;
import com.example.urlshortener.service.utils.ShortenerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShortenerService {

    private final AccountRepository accountRepository;
    private final RegisteredUrlRepository registeredUrlRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountResponseDto createAccount(String accountId) {
        if (accountRepository.existsById(accountId)) {
            return AccountResponseDto.builder()
                    .success(false)
                    .description("Account already exists")
                    .password(null).build();
        }

        var rawPassword = ShortenerUtils.generateAlphanumericString(ShortenerUtils.PASSWORD_LENGTH);
        accountRepository.save(Account.builder()
                .id(accountId)
                .password(passwordEncoder.encode(rawPassword)).build()
        );

        return AccountResponseDto.builder()
                .success(true)
                .description("Account has been successfully created")
                .password(rawPassword).build();
    }

    public RegisterResponseDto registerUrl(RegisterRequestDto requestDto, String accountId) {
        if (requestDto.getRedirectType() != HttpStatus.MOVED_PERMANENTLY.value()) {
            requestDto.setRedirectType(HttpStatus.FOUND.value());
        }
        var registeredUrl = registeredUrlRepository.save(
                RegisteredUrl.builder()
                        .originUrl(requestDto.getUrl())
                        .account(Account.builder().id(accountId).build())
                        .shortenUrlId(getUniqueShortenUrlId())
                        .redirectType(requestDto.getRedirectType())
                        .callsCount(0).build()
        );

        var baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        var shortenUrl = String.format("%s/%s", baseUrl, registeredUrl.getShortenUrlId());
        return RegisterResponseDto.builder().shortUrl(shortenUrl).build();
    }

    private String getUniqueShortenUrlId() {
        String shortenUrlId;
        do {
            shortenUrlId = ShortenerUtils.generateAlphanumericString(ShortenerUtils.URL_ID_LENGTH);
        } while (registeredUrlRepository.existsRegisteredUrlByShortenUrlId(shortenUrlId));

        return shortenUrlId;
    }

    public List<AbstractMap.SimpleImmutableEntry<String, Integer>> getStatistic(String accountId) {
        List<RegisteredUrl> accountUrls = registeredUrlRepository.findByAccount_Id(accountId);

        var statistic = accountUrls.stream()
                .map(registeredUrl -> new AbstractMap.SimpleImmutableEntry<>(registeredUrl.getOriginUrl(), registeredUrl.getCallsCount()))
                .collect(Collectors.toList());
        return statistic;
    }

    public RegisteredUrl getRegisteredUrl(String urlId) {
        RegisteredUrl registeredUrl = registeredUrlRepository.findByShortenUrlId(urlId);
        if (registeredUrl != null) {
            registeredUrl.setCallsCount(registeredUrl.getCallsCount() + 1);
            return registeredUrlRepository.save(registeredUrl);
        }
        return null;
    }
}
