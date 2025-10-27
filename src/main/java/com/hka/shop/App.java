package com.hka.shop;

import com.hka.shop.domain.OrderMocks;
import com.hka.shop.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  CommandLineRunner loadSampleOrders(OrderService service) {
    return args -> OrderMocks.sampleOrders().forEach(order -> {
      try {
        service.create(order);
      } catch (IllegalStateException ignored) {
      }
    });
  }
}
