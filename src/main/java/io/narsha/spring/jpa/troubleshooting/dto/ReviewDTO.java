package io.narsha.spring.jpa.troubleshooting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {

    private BookDTO book;
    private String summary;
    private Double score;
    private String description;
    private UserInfoDTO userInfo;

    public ReviewDTO(String bookId, String bookTitle, String summary, Double score, String description, String userId, String username, String country, String region, String continent) {
        this.book = new BookDTO();
        this.book.setId(bookId);
        this.book.setTitle(bookTitle);
        this.summary = summary;
        this.score = score;
        this.description = description;
        this.userInfo = new UserInfoDTO();
        this.userInfo.setId(userId);
        this.userInfo.setUsername(username);
        this.userInfo.setCountry(country);
        this.userInfo.setRegion(region);
        this.userInfo.setContinent(continent);
    }
}
