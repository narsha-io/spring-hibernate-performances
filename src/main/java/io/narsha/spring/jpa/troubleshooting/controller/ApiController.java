package io.narsha.spring.jpa.troubleshooting.controller;

import io.narsha.spring.jpa.troubleshooting.aop.Timer;
import io.narsha.spring.jpa.troubleshooting.dto.ReviewDTO;
import io.narsha.spring.jpa.troubleshooting.entity.ReviewId;
import io.narsha.spring.jpa.troubleshooting.mapper.ReviewMapper;
import io.narsha.spring.jpa.troubleshooting.service.ReviewService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class ApiController {

    private final ReviewMapper mapper;
    private final ReviewService service;

    @Timer
    @GetMapping("/v1/reviews")
    public ResponseEntity<ReviewDTO> byId(@RequestParam String userId, @RequestParam String bookId) {
        return ResponseEntity.of(service.findById(new ReviewId(bookId, userId)).map(mapper::toDTO));
    }

    @Timer
    @GetMapping("/v1/reviews/search")
    public ResponseEntity<List<ReviewDTO>> searchV1(@RequestParam String q) {
        return ResponseEntity.ok(service.findByUserReview(q).stream().map(mapper::toDTO).toList());
    }

    @Timer
    @GetMapping("/v2/reviews/search")
    public ResponseEntity<List<ReviewDTO>> searchV2(@RequestParam String q) {
        return ResponseEntity.ok(service.findByUserReviewV2(q).stream().map(mapper::toDTO).toList());
    }

    @Timer
    @GetMapping("/v3/reviews/search")
    public ResponseEntity<List<ReviewDTO>> searchV3(@RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(service.findByUserReviewV3(q, pageable).stream().map(mapper::toDTO).toList());
    }

    @Timer
    @SneakyThrows
    @GetMapping("/v4/reviews/search")
    public void searchV4(@RequestParam String q, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        service.findByUserReviewV4(q, response.getOutputStream());
    }

    @Timer
    @SneakyThrows
    @GetMapping("/v5/reviews/search")
    public void searchV5(@RequestParam String q, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        service.findByUserReviewV5(q, response.getOutputStream());
    }
}
