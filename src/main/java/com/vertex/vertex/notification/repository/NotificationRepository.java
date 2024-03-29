package com.vertex.vertex.notification.repository;

import com.vertex.vertex.notification.entity.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Page<Notification> findAll(Pageable pageable);

    List<Notification> findAllByUser_IdOrderByIdDesc(Long userID);

}
