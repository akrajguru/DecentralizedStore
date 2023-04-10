package chord;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.49.1)",
    comments = "Source: chord.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SendReceiveGrpc {

  private SendReceiveGrpc() {}

  public static final String SERVICE_NAME = "chord.SendReceive";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<chord.Chord.BytesRequest,
      chord.Chord.BytesResponse> getSendBytesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendBytes",
      requestType = chord.Chord.BytesRequest.class,
      responseType = chord.Chord.BytesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.BytesRequest,
      chord.Chord.BytesResponse> getSendBytesMethod() {
    io.grpc.MethodDescriptor<chord.Chord.BytesRequest, chord.Chord.BytesResponse> getSendBytesMethod;
    if ((getSendBytesMethod = SendReceiveGrpc.getSendBytesMethod) == null) {
      synchronized (SendReceiveGrpc.class) {
        if ((getSendBytesMethod = SendReceiveGrpc.getSendBytesMethod) == null) {
          SendReceiveGrpc.getSendBytesMethod = getSendBytesMethod =
              io.grpc.MethodDescriptor.<chord.Chord.BytesRequest, chord.Chord.BytesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendBytes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.BytesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.BytesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SendReceiveMethodDescriptorSupplier("SendBytes"))
              .build();
        }
      }
    }
    return getSendBytesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.FDRequest,
      chord.Chord.FDResponse> getSendFDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendFD",
      requestType = chord.Chord.FDRequest.class,
      responseType = chord.Chord.FDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.FDRequest,
      chord.Chord.FDResponse> getSendFDMethod() {
    io.grpc.MethodDescriptor<chord.Chord.FDRequest, chord.Chord.FDResponse> getSendFDMethod;
    if ((getSendFDMethod = SendReceiveGrpc.getSendFDMethod) == null) {
      synchronized (SendReceiveGrpc.class) {
        if ((getSendFDMethod = SendReceiveGrpc.getSendFDMethod) == null) {
          SendReceiveGrpc.getSendFDMethod = getSendFDMethod =
              io.grpc.MethodDescriptor.<chord.Chord.FDRequest, chord.Chord.FDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendFD"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.FDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.FDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SendReceiveMethodDescriptorSupplier("SendFD"))
              .build();
        }
      }
    }
    return getSendFDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.RetRequest,
      chord.Chord.RetResponse> getRetrieveFileRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "retrieveFileRequest",
      requestType = chord.Chord.RetRequest.class,
      responseType = chord.Chord.RetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.RetRequest,
      chord.Chord.RetResponse> getRetrieveFileRequestMethod() {
    io.grpc.MethodDescriptor<chord.Chord.RetRequest, chord.Chord.RetResponse> getRetrieveFileRequestMethod;
    if ((getRetrieveFileRequestMethod = SendReceiveGrpc.getRetrieveFileRequestMethod) == null) {
      synchronized (SendReceiveGrpc.class) {
        if ((getRetrieveFileRequestMethod = SendReceiveGrpc.getRetrieveFileRequestMethod) == null) {
          SendReceiveGrpc.getRetrieveFileRequestMethod = getRetrieveFileRequestMethod =
              io.grpc.MethodDescriptor.<chord.Chord.RetRequest, chord.Chord.RetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "retrieveFileRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.RetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.RetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SendReceiveMethodDescriptorSupplier("retrieveFileRequest"))
              .build();
        }
      }
    }
    return getRetrieveFileRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.sendDataToOwner,
      chord.Chord.acknowledgement> getSendDataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendData",
      requestType = chord.Chord.sendDataToOwner.class,
      responseType = chord.Chord.acknowledgement.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.sendDataToOwner,
      chord.Chord.acknowledgement> getSendDataMethod() {
    io.grpc.MethodDescriptor<chord.Chord.sendDataToOwner, chord.Chord.acknowledgement> getSendDataMethod;
    if ((getSendDataMethod = SendReceiveGrpc.getSendDataMethod) == null) {
      synchronized (SendReceiveGrpc.class) {
        if ((getSendDataMethod = SendReceiveGrpc.getSendDataMethod) == null) {
          SendReceiveGrpc.getSendDataMethod = getSendDataMethod =
              io.grpc.MethodDescriptor.<chord.Chord.sendDataToOwner, chord.Chord.acknowledgement>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendData"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.sendDataToOwner.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.acknowledgement.getDefaultInstance()))
              .setSchemaDescriptor(new SendReceiveMethodDescriptorSupplier("sendData"))
              .build();
        }
      }
    }
    return getSendDataMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.sendReplica,
      chord.Chord.replicaAck> getReplicateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "replicate",
      requestType = chord.Chord.sendReplica.class,
      responseType = chord.Chord.replicaAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.sendReplica,
      chord.Chord.replicaAck> getReplicateMethod() {
    io.grpc.MethodDescriptor<chord.Chord.sendReplica, chord.Chord.replicaAck> getReplicateMethod;
    if ((getReplicateMethod = SendReceiveGrpc.getReplicateMethod) == null) {
      synchronized (SendReceiveGrpc.class) {
        if ((getReplicateMethod = SendReceiveGrpc.getReplicateMethod) == null) {
          SendReceiveGrpc.getReplicateMethod = getReplicateMethod =
              io.grpc.MethodDescriptor.<chord.Chord.sendReplica, chord.Chord.replicaAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "replicate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.sendReplica.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.replicaAck.getDefaultInstance()))
              .setSchemaDescriptor(new SendReceiveMethodDescriptorSupplier("replicate"))
              .build();
        }
      }
    }
    return getReplicateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SendReceiveStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SendReceiveStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SendReceiveStub>() {
        @java.lang.Override
        public SendReceiveStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SendReceiveStub(channel, callOptions);
        }
      };
    return SendReceiveStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SendReceiveBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SendReceiveBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SendReceiveBlockingStub>() {
        @java.lang.Override
        public SendReceiveBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SendReceiveBlockingStub(channel, callOptions);
        }
      };
    return SendReceiveBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SendReceiveFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SendReceiveFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SendReceiveFutureStub>() {
        @java.lang.Override
        public SendReceiveFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SendReceiveFutureStub(channel, callOptions);
        }
      };
    return SendReceiveFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class SendReceiveImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendBytes(chord.Chord.BytesRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.BytesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendBytesMethod(), responseObserver);
    }

    /**
     */
    public void sendFD(chord.Chord.FDRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.FDResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendFDMethod(), responseObserver);
    }

    /**
     */
    public void retrieveFileRequest(chord.Chord.RetRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.RetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRetrieveFileRequestMethod(), responseObserver);
    }

    /**
     */
    public void sendData(chord.Chord.sendDataToOwner request,
        io.grpc.stub.StreamObserver<chord.Chord.acknowledgement> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendDataMethod(), responseObserver);
    }

    /**
     */
    public void replicate(chord.Chord.sendReplica request,
        io.grpc.stub.StreamObserver<chord.Chord.replicaAck> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReplicateMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendBytesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.BytesRequest,
                chord.Chord.BytesResponse>(
                  this, METHODID_SEND_BYTES)))
          .addMethod(
            getSendFDMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.FDRequest,
                chord.Chord.FDResponse>(
                  this, METHODID_SEND_FD)))
          .addMethod(
            getRetrieveFileRequestMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.RetRequest,
                chord.Chord.RetResponse>(
                  this, METHODID_RETRIEVE_FILE_REQUEST)))
          .addMethod(
            getSendDataMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.sendDataToOwner,
                chord.Chord.acknowledgement>(
                  this, METHODID_SEND_DATA)))
          .addMethod(
            getReplicateMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.sendReplica,
                chord.Chord.replicaAck>(
                  this, METHODID_REPLICATE)))
          .build();
    }
  }

  /**
   */
  public static final class SendReceiveStub extends io.grpc.stub.AbstractAsyncStub<SendReceiveStub> {
    private SendReceiveStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SendReceiveStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SendReceiveStub(channel, callOptions);
    }

    /**
     */
    public void sendBytes(chord.Chord.BytesRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.BytesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendBytesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendFD(chord.Chord.FDRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.FDResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendFDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void retrieveFileRequest(chord.Chord.RetRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.RetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRetrieveFileRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendData(chord.Chord.sendDataToOwner request,
        io.grpc.stub.StreamObserver<chord.Chord.acknowledgement> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendDataMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void replicate(chord.Chord.sendReplica request,
        io.grpc.stub.StreamObserver<chord.Chord.replicaAck> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReplicateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SendReceiveBlockingStub extends io.grpc.stub.AbstractBlockingStub<SendReceiveBlockingStub> {
    private SendReceiveBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SendReceiveBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SendReceiveBlockingStub(channel, callOptions);
    }

    /**
     */
    public chord.Chord.BytesResponse sendBytes(chord.Chord.BytesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendBytesMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.FDResponse sendFD(chord.Chord.FDRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendFDMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.RetResponse retrieveFileRequest(chord.Chord.RetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRetrieveFileRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.acknowledgement sendData(chord.Chord.sendDataToOwner request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendDataMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.replicaAck replicate(chord.Chord.sendReplica request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReplicateMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SendReceiveFutureStub extends io.grpc.stub.AbstractFutureStub<SendReceiveFutureStub> {
    private SendReceiveFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SendReceiveFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SendReceiveFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.BytesResponse> sendBytes(
        chord.Chord.BytesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendBytesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.FDResponse> sendFD(
        chord.Chord.FDRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendFDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.RetResponse> retrieveFileRequest(
        chord.Chord.RetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRetrieveFileRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.acknowledgement> sendData(
        chord.Chord.sendDataToOwner request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendDataMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.replicaAck> replicate(
        chord.Chord.sendReplica request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReplicateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_BYTES = 0;
  private static final int METHODID_SEND_FD = 1;
  private static final int METHODID_RETRIEVE_FILE_REQUEST = 2;
  private static final int METHODID_SEND_DATA = 3;
  private static final int METHODID_REPLICATE = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SendReceiveImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SendReceiveImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_BYTES:
          serviceImpl.sendBytes((chord.Chord.BytesRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.BytesResponse>) responseObserver);
          break;
        case METHODID_SEND_FD:
          serviceImpl.sendFD((chord.Chord.FDRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.FDResponse>) responseObserver);
          break;
        case METHODID_RETRIEVE_FILE_REQUEST:
          serviceImpl.retrieveFileRequest((chord.Chord.RetRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.RetResponse>) responseObserver);
          break;
        case METHODID_SEND_DATA:
          serviceImpl.sendData((chord.Chord.sendDataToOwner) request,
              (io.grpc.stub.StreamObserver<chord.Chord.acknowledgement>) responseObserver);
          break;
        case METHODID_REPLICATE:
          serviceImpl.replicate((chord.Chord.sendReplica) request,
              (io.grpc.stub.StreamObserver<chord.Chord.replicaAck>) responseObserver);
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

  private static abstract class SendReceiveBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SendReceiveBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return chord.Chord.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SendReceive");
    }
  }

  private static final class SendReceiveFileDescriptorSupplier
      extends SendReceiveBaseDescriptorSupplier {
    SendReceiveFileDescriptorSupplier() {}
  }

  private static final class SendReceiveMethodDescriptorSupplier
      extends SendReceiveBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SendReceiveMethodDescriptorSupplier(String methodName) {
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
      synchronized (SendReceiveGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SendReceiveFileDescriptorSupplier())
              .addMethod(getSendBytesMethod())
              .addMethod(getSendFDMethod())
              .addMethod(getRetrieveFileRequestMethod())
              .addMethod(getSendDataMethod())
              .addMethod(getReplicateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
