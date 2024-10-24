package com.recipe.likes;

import org.hibernate.annotations.Comment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class listener {

    @KafkaListener(topics = "like-events",groupId = "groupId")
    public void readData(String data){
        System.out.println("Listener received " +data);
    }
}
