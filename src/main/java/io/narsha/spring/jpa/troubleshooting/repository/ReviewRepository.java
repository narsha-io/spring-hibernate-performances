package io.narsha.spring.jpa.troubleshooting.repository;

import io.narsha.spring.jpa.troubleshooting.dto.ReviewDTO;
import io.narsha.spring.jpa.troubleshooting.entity.Review;
import io.narsha.spring.jpa.troubleshooting.entity.ReviewId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewId> {

    @Query("""
        select r
        from Review r
        where r.user.username
        ilike :userId
        order by r.book.id asc,
        r.user.id asc""")
    List<Review> findByUserReview0(String userId);

    @Query("""
        select r 
        from Review r 
        join fetch r.user u 
        join fetch r.book 
        join fetch u.country c 
        join fetch c.region r2 
        join fetch r2.continent 
        where r.user.username  
        ilike :userId 
        order by r.book.id asc, 
        r.user.id asc""")
    List<Review> findByUserReview1(String userId);

    @Query("select r from Review r join fetch r.user u join fetch r.book join fetch u.country c join fetch c.region r2 join fetch r2.continent where r.user.username  ilike :userId")
    List<Review> findByUserReviewV3(String userId, Pageable pageable);

    @Query("select r from Review r join fetch r.user u join fetch r.book join fetch u.country c join fetch c.region r2 join fetch r2.continent where r.user.username  ilike :userId order by r.book.id asc, r.user.id asc")
    Stream<Review> findByUserReviewV4(String userId);

    @Query("select new io.narsha.spring.jpa.troubleshooting.dto.ReviewDTO(b.id, b.title, r.summary, r.score, r.description, u.id, u.username, c.name, r2.name, c2.name) from Review r join r.user u join r.book b join u.country c join  c.region r2 join r2.continent c2 where u.username  ilike :userId order by r.book.id asc, r.user.id asc")
    Stream<ReviewDTO> findByUserReviewV5(String userId);
}
