package com.vertex.vertex.user.repository;

import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
    User findByFirstName(String firstName);
    boolean existsByEmail(String email);

//    List<Notification> findAllByIdAndNotifications_IdOrderByIdDesc(Long userID);
//    List<Notification> findAll(Long userID);
}
