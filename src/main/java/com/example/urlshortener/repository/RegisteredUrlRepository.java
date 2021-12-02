package com.example.urlshortener.repository;

import com.example.urlshortener.model.entity.RegisteredUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisteredUrlRepository extends JpaRepository<RegisteredUrl, String> {

    boolean existsRegisteredUrlByShortenUrlId(String shortenUrlId);

    RegisteredUrl findByShortenUrlId(String shortenUrlId);

    List<RegisteredUrl> findByAccount_Id(String accountId);
}
