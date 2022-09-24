package com.tweetapp.Kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ConsumerService {
//	public static final String TOPIC_NAME = "tweetappNew";
//	public static final String GROUP_ID = "mygroup";
//
//	@KafkaListener(topics = "tweetappNew", groupId = "mygroup")
//	public String consume(String message) {
//		log.debug("Consumed message {}",message);
//		log.debug("Message recieved -> {}",message);
//		return  "Message recieved:"+message;
//	}
}