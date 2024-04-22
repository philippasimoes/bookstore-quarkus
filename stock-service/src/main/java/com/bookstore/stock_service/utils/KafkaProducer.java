package com.bookstore.stock_service.utils;

import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

public class KafkaProducer {

  @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10)
  @Inject
  @Channel("available-out")
  Emitter<String> emitterForAvailableBooks;

  @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10)
  @Inject
  @Channel("soldout-out")
  Emitter<String> emitterForSoldOutBooks;

  public void send(String topic, String message) {

    if (topic.equals("available")) {
      emitterForAvailableBooks.send(Message.of(message));
    } else {
      emitterForSoldOutBooks.send(Message.of(message));
    }
  }
}
