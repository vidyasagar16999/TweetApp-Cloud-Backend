package com.tweetapp.Kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ProducerService {
//	public static final String TOPIC_NAME = "tweetappNew";
//
//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;
//
//	public void sendMessage(String message) {
//		log.debug("Publishing to topic {} ", TOPIC_NAME);
//		log.debug(String.format("Message sent -> %s", message));
//		kafkaTemplate.send(TOPIC_NAME, message);
//
//	}
}