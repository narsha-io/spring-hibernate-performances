package io.narsha.spring.jpa.troubleshooting.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.sql.Blob;

@Entity
@Data
@Table(name = "review")
public class Review {

    @EmbeddedId
    private ReviewId id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false, insertable = false, updatable = false)
    private Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = "summary", nullable = false, length = 500)
    private String summary;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "score", nullable = false)
    private Double score;

}
