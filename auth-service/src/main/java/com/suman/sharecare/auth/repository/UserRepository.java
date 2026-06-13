package com.suman.sharecare.auth.repository;

import com.suman.sharecare.auth.entity.User;
import org.bouncycastle.crypto.examples.JPAKEExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
