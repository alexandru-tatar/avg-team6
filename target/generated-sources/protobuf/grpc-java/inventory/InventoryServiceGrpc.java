package inventory;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class InventoryServiceGrpc {

  private InventoryServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "inventory.InventoryService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<inventory.Inventory.CheckAvailabilityRequest,
      inventory.Inventory.CheckAvailabilityResponse> getCheckAvailabilityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckAvailability",
      requestType = inventory.Inventory.CheckAvailabilityRequest.class,
      responseType = inventory.Inventory.CheckAvailabilityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<inventory.Inventory.CheckAvailabilityRequest,
      inventory.Inventory.CheckAvailabilityResponse> getCheckAvailabilityMethod() {
    io.grpc.MethodDescriptor<inventory.Inventory.CheckAvailabilityRequest, inventory.Inventory.CheckAvailabilityResponse> getCheckAvailabilityMethod;
    if ((getCheckAvailabilityMethod = InventoryServiceGrpc.getCheckAvailabilityMethod) == null) {
      synchronized (InventoryServiceGrpc.class) {
        if ((getCheckAvailabilityMethod = InventoryServiceGrpc.getCheckAvailabilityMethod) == null) {
          InventoryServiceGrpc.getCheckAvailabilityMethod = getCheckAvailabilityMethod =
              io.grpc.MethodDescriptor.<inventory.Inventory.CheckAvailabilityRequest, inventory.Inventory.CheckAvailabilityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckAvailability"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  inventory.Inventory.CheckAvailabilityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  inventory.Inventory.CheckAvailabilityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new InventoryServiceMethodDescriptorSupplier("CheckAvailability"))
              .build();
        }
      }
    }
    return getCheckAvailabilityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<inventory.Inventory.ReserveItemsRequest,
      inventory.Inventory.ReserveItemsResponse> getReserveItemsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReserveItems",
      requestType = inventory.Inventory.ReserveItemsRequest.class,
      responseType = inventory.Inventory.ReserveItemsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<inventory.Inventory.ReserveItemsRequest,
      inventory.Inventory.ReserveItemsResponse> getReserveItemsMethod() {
    io.grpc.MethodDescriptor<inventory.Inventory.ReserveItemsRequest, inventory.Inventory.ReserveItemsResponse> getReserveItemsMethod;
    if ((getReserveItemsMethod = InventoryServiceGrpc.getReserveItemsMethod) == null) {
      synchronized (InventoryServiceGrpc.class) {
        if ((getReserveItemsMethod = InventoryServiceGrpc.getReserveItemsMethod) == null) {
          InventoryServiceGrpc.getReserveItemsMethod = getReserveItemsMethod =
              io.grpc.MethodDescriptor.<inventory.Inventory.ReserveItemsRequest, inventory.Inventory.ReserveItemsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReserveItems"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  inventory.Inventory.ReserveItemsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  inventory.Inventory.ReserveItemsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new InventoryServiceMethodDescriptorSupplier("ReserveItems"))
              .build();
        }
      }
    }
    return getReserveItemsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<inventory.Inventory.ReleaseReservationRequest,
      inventory.Inventory.ReleaseReservationResponse> getReleaseReservationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReleaseReservation",
      requestType = inventory.Inventory.ReleaseReservationRequest.class,
      responseType = inventory.Inventory.ReleaseReservationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<inventory.Inventory.ReleaseReservationRequest,
      inventory.Inventory.ReleaseReservationResponse> getReleaseReservationMethod() {
    io.grpc.MethodDescriptor<inventory.Inventory.ReleaseReservationRequest, inventory.Inventory.ReleaseReservationResponse> getReleaseReservationMethod;
    if ((getReleaseReservationMethod = InventoryServiceGrpc.getReleaseReservationMethod) == null) {
      synchronized (InventoryServiceGrpc.class) {
        if ((getReleaseReservationMethod = InventoryServiceGrpc.getReleaseReservationMethod) == null) {
          InventoryServiceGrpc.getReleaseReservationMethod = getReleaseReservationMethod =
              io.grpc.MethodDescriptor.<inventory.Inventory.ReleaseReservationRequest, inventory.Inventory.ReleaseReservationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReleaseReservation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  inventory.Inventory.ReleaseReservationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  inventory.Inventory.ReleaseReservationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new InventoryServiceMethodDescriptorSupplier("ReleaseReservation"))
              .build();
        }
      }
    }
    return getReleaseReservationMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static InventoryServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InventoryServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InventoryServiceStub>() {
        @java.lang.Override
        public InventoryServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InventoryServiceStub(channel, callOptions);
        }
      };
    return InventoryServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static InventoryServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InventoryServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InventoryServiceBlockingV2Stub>() {
        @java.lang.Override
        public InventoryServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InventoryServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return InventoryServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static InventoryServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InventoryServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InventoryServiceBlockingStub>() {
        @java.lang.Override
        public InventoryServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InventoryServiceBlockingStub(channel, callOptions);
        }
      };
    return InventoryServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static InventoryServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InventoryServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InventoryServiceFutureStub>() {
        @java.lang.Override
        public InventoryServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InventoryServiceFutureStub(channel, callOptions);
        }
      };
    return InventoryServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void checkAvailability(inventory.Inventory.CheckAvailabilityRequest request,
        io.grpc.stub.StreamObserver<inventory.Inventory.CheckAvailabilityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckAvailabilityMethod(), responseObserver);
    }

    /**
     */
    default void reserveItems(inventory.Inventory.ReserveItemsRequest request,
        io.grpc.stub.StreamObserver<inventory.Inventory.ReserveItemsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReserveItemsMethod(), responseObserver);
    }

    /**
     */
    default void releaseReservation(inventory.Inventory.ReleaseReservationRequest request,
        io.grpc.stub.StreamObserver<inventory.Inventory.ReleaseReservationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReleaseReservationMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service InventoryService.
   */
  public static abstract class InventoryServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return InventoryServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service InventoryService.
   */
  public static final class InventoryServiceStub
      extends io.grpc.stub.AbstractAsyncStub<InventoryServiceStub> {
    private InventoryServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InventoryServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InventoryServiceStub(channel, callOptions);
    }

    /**
     */
    public void checkAvailability(inventory.Inventory.CheckAvailabilityRequest request,
        io.grpc.stub.StreamObserver<inventory.Inventory.CheckAvailabilityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckAvailabilityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reserveItems(inventory.Inventory.ReserveItemsRequest request,
        io.grpc.stub.StreamObserver<inventory.Inventory.ReserveItemsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReserveItemsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void releaseReservation(inventory.Inventory.ReleaseReservationRequest request,
        io.grpc.stub.StreamObserver<inventory.Inventory.ReleaseReservationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReleaseReservationMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service InventoryService.
   */
  public static final class InventoryServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<InventoryServiceBlockingV2Stub> {
    private InventoryServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InventoryServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InventoryServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public inventory.Inventory.CheckAvailabilityResponse checkAvailability(inventory.Inventory.CheckAvailabilityRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCheckAvailabilityMethod(), getCallOptions(), request);
    }

    /**
     */
    public inventory.Inventory.ReserveItemsResponse reserveItems(inventory.Inventory.ReserveItemsRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getReserveItemsMethod(), getCallOptions(), request);
    }

    /**
     */
    public inventory.Inventory.ReleaseReservationResponse releaseReservation(inventory.Inventory.ReleaseReservationRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getReleaseReservationMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service InventoryService.
   */
  public static final class InventoryServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<InventoryServiceBlockingStub> {
    private InventoryServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InventoryServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InventoryServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public inventory.Inventory.CheckAvailabilityResponse checkAvailability(inventory.Inventory.CheckAvailabilityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckAvailabilityMethod(), getCallOptions(), request);
    }

    /**
     */
    public inventory.Inventory.ReserveItemsResponse reserveItems(inventory.Inventory.ReserveItemsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReserveItemsMethod(), getCallOptions(), request);
    }

    /**
     */
    public inventory.Inventory.ReleaseReservationResponse releaseReservation(inventory.Inventory.ReleaseReservationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReleaseReservationMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service InventoryService.
   */
  public static final class InventoryServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<InventoryServiceFutureStub> {
    private InventoryServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InventoryServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InventoryServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<inventory.Inventory.CheckAvailabilityResponse> checkAvailability(
        inventory.Inventory.CheckAvailabilityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckAvailabilityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<inventory.Inventory.ReserveItemsResponse> reserveItems(
        inventory.Inventory.ReserveItemsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReserveItemsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<inventory.Inventory.ReleaseReservationResponse> releaseReservation(
        inventory.Inventory.ReleaseReservationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReleaseReservationMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHECK_AVAILABILITY = 0;
  private static final int METHODID_RESERVE_ITEMS = 1;
  private static final int METHODID_RELEASE_RESERVATION = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHECK_AVAILABILITY:
          serviceImpl.checkAvailability((inventory.Inventory.CheckAvailabilityRequest) request,
              (io.grpc.stub.StreamObserver<inventory.Inventory.CheckAvailabilityResponse>) responseObserver);
          break;
        case METHODID_RESERVE_ITEMS:
          serviceImpl.reserveItems((inventory.Inventory.ReserveItemsRequest) request,
              (io.grpc.stub.StreamObserver<inventory.Inventory.ReserveItemsResponse>) responseObserver);
          break;
        case METHODID_RELEASE_RESERVATION:
          serviceImpl.releaseReservation((inventory.Inventory.ReleaseReservationRequest) request,
              (io.grpc.stub.StreamObserver<inventory.Inventory.ReleaseReservationResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCheckAvailabilityMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              inventory.Inventory.CheckAvailabilityRequest,
              inventory.Inventory.CheckAvailabilityResponse>(
                service, METHODID_CHECK_AVAILABILITY)))
        .addMethod(
          getReserveItemsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              inventory.Inventory.ReserveItemsRequest,
              inventory.Inventory.ReserveItemsResponse>(
                service, METHODID_RESERVE_ITEMS)))
        .addMethod(
          getReleaseReservationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              inventory.Inventory.ReleaseReservationRequest,
              inventory.Inventory.ReleaseReservationResponse>(
                service, METHODID_RELEASE_RESERVATION)))
        .build();
  }

  private static abstract class InventoryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    InventoryServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return inventory.Inventory.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("InventoryService");
    }
  }

  private static final class InventoryServiceFileDescriptorSupplier
      extends InventoryServiceBaseDescriptorSupplier {
    InventoryServiceFileDescriptorSupplier() {}
  }

  private static final class InventoryServiceMethodDescriptorSupplier
      extends InventoryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    InventoryServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (InventoryServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new InventoryServiceFileDescriptorSupplier())
              .addMethod(getCheckAvailabilityMethod())
              .addMethod(getReserveItemsMethod())
              .addMethod(getReleaseReservationMethod())
              .build();
        }
      }
    }
    return result;
  }
}
