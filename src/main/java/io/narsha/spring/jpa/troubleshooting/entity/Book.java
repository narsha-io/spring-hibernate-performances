package io.narsha.spring.jpa.troubleshooting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id", nullable = false, length = 200)
    private String id;

    @Column(name = "title", nullable = false, length = 500)
    private String title;
}
