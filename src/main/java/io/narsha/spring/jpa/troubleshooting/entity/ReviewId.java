package io.narsha.spring.jpa.troubleshooting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ReviewId implements Serializable {

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(name = "user_id", nullable = false)
    private String userId;

}
