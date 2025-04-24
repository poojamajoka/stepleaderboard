package com.stepthrone.userprofile.repository;

import com.stepthrone.userprofile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByid(Long id);
    Optional<User> findByUsername(String username);
   // void updateAllUser(List<User> users);
}
