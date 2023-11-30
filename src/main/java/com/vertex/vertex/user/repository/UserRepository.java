package com.vertex.vertex.user.repository;

import com.vertex.vertex.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

}
