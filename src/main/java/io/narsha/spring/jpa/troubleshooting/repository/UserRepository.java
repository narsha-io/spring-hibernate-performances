package io.narsha.spring.jpa.troubleshooting.repository;

import io.narsha.spring.jpa.troubleshooting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
