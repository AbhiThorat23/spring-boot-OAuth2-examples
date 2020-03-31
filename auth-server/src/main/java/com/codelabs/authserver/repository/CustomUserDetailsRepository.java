package com.codelabs.authserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codelabs.authserver.models.User;

@Repository
public interface CustomUserDetailsRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String name);
}
