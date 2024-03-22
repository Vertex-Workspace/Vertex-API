package com.vertex.vertex.notification.entity.service;


import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;


    public Notification save(Notification notification){
        return notificationRepository.save(notification);
    }

    public void delete(Notification notification){
        notificationRepository.delete(notification);
    }


    public List<Notification> getNotificationsByUser(Long userID){
        return notificationRepository.findAllByUser_IdOrderByIdDesc(userID);
    }





}
