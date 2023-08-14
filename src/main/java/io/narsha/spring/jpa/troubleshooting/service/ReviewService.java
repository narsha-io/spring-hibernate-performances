package io.narsha.spring.jpa.troubleshooting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.spring.jpa.troubleshooting.NewReviewRecord;
import io.narsha.spring.jpa.troubleshooting.entity.Review;
import io.narsha.spring.jpa.troubleshooting.entity.ReviewId;
import io.narsha.spring.jpa.troubleshooting.mapper.ReviewMapper;
import io.narsha.spring.jpa.troubleshooting.repository.BookRepository;
import io.narsha.spring.jpa.troubleshooting.repository.ReviewRepository;
import io.narsha.spring.jpa.troubleshooting.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private final ObjectMapper objectMapper;
    private final ReviewMapper reviewMapper;

    public Optional<Review> findById(ReviewId id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findByUserReview(String userId) {
        return reviewRepository.findByUserReview0("%"+userId+"%");
    }

    public List<Review> findByUserReviewV2(String userId) {
        return reviewRepository.findByUserReview1("%"+userId+"%");
    }

    public List<Review> findByUserReviewV3(String userId, Pageable pageable) {
        return reviewRepository.findByUserReviewV3("%"+userId+"%", pageable);
    }

    @SneakyThrows
    @Transactional
    public void findByUserReviewV4(String userId, OutputStream output) {
        var jsonGenerator = objectMapper.getFactory().createGenerator(output);
        try (var stream = reviewRepository.findByUserReviewV4("%"+userId+"%")) {

            jsonGenerator.writeStartArray();
            var it = stream.iterator();
            while(it.hasNext()) {
                var e = it.next();
                var dto = reviewMapper.toDTO(e);
                jsonGenerator.writeObject(dto);
            }
            jsonGenerator.writeEndArray();
        } finally {
            if (jsonGenerator != null) {
                Unchecked.runnable(jsonGenerator::close);
            }
        }
    }

    @SneakyThrows
    @Transactional
    public void findByUserReviewV5(String userId, OutputStream output) {
        var jsonGenerator = objectMapper.getFactory().createGenerator(output);
        try (var stream = reviewRepository.findByUserReviewV5("%"+userId+"%")) {

            jsonGenerator.writeStartArray();
            var it = stream.iterator();
            while(it.hasNext()) {
                var dto = it.next();
                jsonGenerator.writeObject(dto);
            }
            jsonGenerator.writeEndArray();
        } finally {
            if (jsonGenerator != null) {
                Unchecked.runnable(jsonGenerator::close);
            }
        }
    }


    @Transactional
    public void save0(NewReviewRecord record) {
        var review = convert.apply(record);
        review.setBook(bookRepository.findById(record.bookId()).orElseThrow());
        review.setUser(userRepository.findById(record.userId()).orElseThrow());
        reviewRepository.save(review);
    }

    @Transactional
    public void save(NewReviewRecord record) {
        var review = convert.apply(record);
        review.setBook(bookRepository.findById(record.bookId()).orElseThrow(() -> new IllegalArgumentException(record.bookId())));
        review.setUser(userRepository.findById(record.userId()).orElseThrow(() -> new IllegalArgumentException(record.userId())));
        save(review);
    }

    @Transactional
    public void save2(NewReviewRecord record) {
        var review = convert.apply(record);
        review.setBook(bookRepository.getReferenceById(record.bookId()));
        review.setUser(userRepository.getReferenceById(record.userId()));
        save(review);
    }

    private void save(Review r) {

    }

    @Transactional
    public void saveAll1(Collection<NewReviewRecord> records) {
        var reviews = records.stream().map(convert).toList();
        reviewRepository.saveAllAndFlush(reviews);
        log.info("{} elements saved", records.size());
    }

    @Transactional
    public void saveAll2(Collection<NewReviewRecord> records) {
        var reviews = records.stream().map(convert).toList();

        reviews.forEach(em::persist);
        em.flush();
        log.info("{} elements saved", records.size());
    }

    Function<NewReviewRecord, Review> convert = in -> {
        var review = new Review();
        review.setId(new ReviewId());
        review.getId().setUserId(in.userId());
        review.getId().setBookId(in.bookId());
        review.setSummary(in.summary());
        review.setDescription(in.description());
        review.setScore(in.score());
        return review;
    };
}
