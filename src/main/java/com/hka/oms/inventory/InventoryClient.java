package com.hka.oms.inventory;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import inventory.Inventory;
import inventory.InventoryServiceGrpc;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hka.oms.domain.Order;
import com.hka.oms.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryClient {
  private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);

  private final ManagedChannel channel;
  private final InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub;

  public InventoryClient(
      @Value("${inventory.grpc.host:localhost}") String host,
      @Value("${inventory.grpc.port:50051}") int port) {
    this.channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();
    this.blockingStub = InventoryServiceGrpc.newBlockingStub(channel);
  }

  public boolean checkAvailability(List<OrderItem> items) {
    Inventory.CheckAvailabilityRequest request = Inventory.CheckAvailabilityRequest.newBuilder()
        .addAllItems(items.stream().map(this::toProtoItem).collect(Collectors.toList()))
        .build();
    try {
      return blockingStub.checkAvailability(request).getAvailable();
    } catch (StatusRuntimeException ex) {
      log.error("Inventory availability check failed", ex);
      throw new IllegalStateException("inventory service unavailable", ex);
    }
  }

  public Inventory.ReserveItemsResponse reserveItems(Order order) {
    Inventory.ReserveItemsRequest request = Inventory.ReserveItemsRequest.newBuilder()
        .setOrderId(order.getOrderId())
        .setCustomerId(order.getCustomer().getCustomerId())
        .addAllItems(order.getItems().stream().map(this::toProtoItem).collect(Collectors.toList()))
        .build();
    try {
      return blockingStub.reserveItems(request);
    } catch (StatusRuntimeException ex) {
      log.error("Inventory reservation failed for order {}", order.getOrderId(), ex);
      throw new IllegalStateException("inventory reservation failed", ex);
    }
  }

  private Inventory.Item toProtoItem(OrderItem item) {
    return Inventory.Item.newBuilder()
        .setProductId(item.getProductId())
        .setQuantity(item.getQuantity())
        .build();
  }

  @PreDestroy
  public void shutdown() {
    channel.shutdownNow();
  }

  public void releaseReservation(String orderId) {
    try {
      blockingStub.releaseReservation(
          Inventory.ReleaseReservationRequest.newBuilder()
              .setOrderId(orderId)
              .build());
      log.info("Released inventory reservation for order {}", orderId);
    } catch (StatusRuntimeException ex) {
      log.error("Failed to release inventory reservation for order {}", orderId, ex);
    }
  }
}
