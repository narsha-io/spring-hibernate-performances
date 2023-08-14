package io.narsha.spring.jpa.troubleshooting;

public record NewReviewRecord(String userId, String bookId, String summary, String description, Double score) {
}
