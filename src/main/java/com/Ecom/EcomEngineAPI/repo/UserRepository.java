package com.Ecom.EcomEngineAPI.repo;

import com.Ecom.EcomEngineAPI.entity.Role;
import com.Ecom.EcomEngineAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    List<User> findByRole(Role role);
    boolean existsByEmail(String email);
    Optional<User> findByUserId(Long userId);
}
