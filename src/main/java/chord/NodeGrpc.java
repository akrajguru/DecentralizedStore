package chord;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.49.1)",
    comments = "Source: chord.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NodeGrpc {

  private NodeGrpc() {}

  public static final String SERVICE_NAME = "chord.Node";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<chord.Chord.ForwardKeyRequest,
      chord.Chord.ForwardKeyReply> getForwardKeyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "forwardKey",
      requestType = chord.Chord.ForwardKeyRequest.class,
      responseType = chord.Chord.ForwardKeyReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.ForwardKeyRequest,
      chord.Chord.ForwardKeyReply> getForwardKeyMethod() {
    io.grpc.MethodDescriptor<chord.Chord.ForwardKeyRequest, chord.Chord.ForwardKeyReply> getForwardKeyMethod;
    if ((getForwardKeyMethod = NodeGrpc.getForwardKeyMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getForwardKeyMethod = NodeGrpc.getForwardKeyMethod) == null) {
          NodeGrpc.getForwardKeyMethod = getForwardKeyMethod =
              io.grpc.MethodDescriptor.<chord.Chord.ForwardKeyRequest, chord.Chord.ForwardKeyReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "forwardKey"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.ForwardKeyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.ForwardKeyReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("forwardKey"))
              .build();
        }
      }
    }
    return getForwardKeyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.NodeInfoRequest,
      chord.Chord.NodeInfoResponse> getGetNodeDataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getNodeData",
      requestType = chord.Chord.NodeInfoRequest.class,
      responseType = chord.Chord.NodeInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.NodeInfoRequest,
      chord.Chord.NodeInfoResponse> getGetNodeDataMethod() {
    io.grpc.MethodDescriptor<chord.Chord.NodeInfoRequest, chord.Chord.NodeInfoResponse> getGetNodeDataMethod;
    if ((getGetNodeDataMethod = NodeGrpc.getGetNodeDataMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getGetNodeDataMethod = NodeGrpc.getGetNodeDataMethod) == null) {
          NodeGrpc.getGetNodeDataMethod = getGetNodeDataMethod =
              io.grpc.MethodDescriptor.<chord.Chord.NodeInfoRequest, chord.Chord.NodeInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getNodeData"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.NodeInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.NodeInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("getNodeData"))
              .build();
        }
      }
    }
    return getGetNodeDataMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.HeartBeatToPredecessorRequest,
      chord.Chord.HeartBeatReplyFromSuccessor> getHeartBeatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "heartBeat",
      requestType = chord.Chord.HeartBeatToPredecessorRequest.class,
      responseType = chord.Chord.HeartBeatReplyFromSuccessor.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.HeartBeatToPredecessorRequest,
      chord.Chord.HeartBeatReplyFromSuccessor> getHeartBeatMethod() {
    io.grpc.MethodDescriptor<chord.Chord.HeartBeatToPredecessorRequest, chord.Chord.HeartBeatReplyFromSuccessor> getHeartBeatMethod;
    if ((getHeartBeatMethod = NodeGrpc.getHeartBeatMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getHeartBeatMethod = NodeGrpc.getHeartBeatMethod) == null) {
          NodeGrpc.getHeartBeatMethod = getHeartBeatMethod =
              io.grpc.MethodDescriptor.<chord.Chord.HeartBeatToPredecessorRequest, chord.Chord.HeartBeatReplyFromSuccessor>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "heartBeat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.HeartBeatToPredecessorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.HeartBeatReplyFromSuccessor.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("heartBeat"))
              .build();
        }
      }
    }
    return getHeartBeatMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.ClosestPFRequest,
      chord.Chord.ClosestPFReply> getClosestPrecedFingerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "closestPrecedFinger",
      requestType = chord.Chord.ClosestPFRequest.class,
      responseType = chord.Chord.ClosestPFReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.ClosestPFRequest,
      chord.Chord.ClosestPFReply> getClosestPrecedFingerMethod() {
    io.grpc.MethodDescriptor<chord.Chord.ClosestPFRequest, chord.Chord.ClosestPFReply> getClosestPrecedFingerMethod;
    if ((getClosestPrecedFingerMethod = NodeGrpc.getClosestPrecedFingerMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getClosestPrecedFingerMethod = NodeGrpc.getClosestPrecedFingerMethod) == null) {
          NodeGrpc.getClosestPrecedFingerMethod = getClosestPrecedFingerMethod =
              io.grpc.MethodDescriptor.<chord.Chord.ClosestPFRequest, chord.Chord.ClosestPFReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "closestPrecedFinger"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.ClosestPFRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.ClosestPFReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("closestPrecedFinger"))
              .build();
        }
      }
    }
    return getClosestPrecedFingerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.UpdateSPRequest,
      chord.Chord.UpdateSPResponse> getUpdateSuccessorPredecessorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateSuccessorPredecessor",
      requestType = chord.Chord.UpdateSPRequest.class,
      responseType = chord.Chord.UpdateSPResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.UpdateSPRequest,
      chord.Chord.UpdateSPResponse> getUpdateSuccessorPredecessorMethod() {
    io.grpc.MethodDescriptor<chord.Chord.UpdateSPRequest, chord.Chord.UpdateSPResponse> getUpdateSuccessorPredecessorMethod;
    if ((getUpdateSuccessorPredecessorMethod = NodeGrpc.getUpdateSuccessorPredecessorMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getUpdateSuccessorPredecessorMethod = NodeGrpc.getUpdateSuccessorPredecessorMethod) == null) {
          NodeGrpc.getUpdateSuccessorPredecessorMethod = getUpdateSuccessorPredecessorMethod =
              io.grpc.MethodDescriptor.<chord.Chord.UpdateSPRequest, chord.Chord.UpdateSPResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateSuccessorPredecessor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.UpdateSPRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.UpdateSPResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("updateSuccessorPredecessor"))
              .build();
        }
      }
    }
    return getUpdateSuccessorPredecessorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.FingerTableUpdateRequest,
      chord.Chord.FingerTableUpdateResponse> getUpdateFingerTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateFingerTable",
      requestType = chord.Chord.FingerTableUpdateRequest.class,
      responseType = chord.Chord.FingerTableUpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.FingerTableUpdateRequest,
      chord.Chord.FingerTableUpdateResponse> getUpdateFingerTableMethod() {
    io.grpc.MethodDescriptor<chord.Chord.FingerTableUpdateRequest, chord.Chord.FingerTableUpdateResponse> getUpdateFingerTableMethod;
    if ((getUpdateFingerTableMethod = NodeGrpc.getUpdateFingerTableMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getUpdateFingerTableMethod = NodeGrpc.getUpdateFingerTableMethod) == null) {
          NodeGrpc.getUpdateFingerTableMethod = getUpdateFingerTableMethod =
              io.grpc.MethodDescriptor.<chord.Chord.FingerTableUpdateRequest, chord.Chord.FingerTableUpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateFingerTable"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.FingerTableUpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.FingerTableUpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("updateFingerTable"))
              .build();
        }
      }
    }
    return getUpdateFingerTableMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.SuccessorCallRequest,
      chord.Chord.SuccessorCallReply> getSuccessorCallMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "successorCall",
      requestType = chord.Chord.SuccessorCallRequest.class,
      responseType = chord.Chord.SuccessorCallReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.SuccessorCallRequest,
      chord.Chord.SuccessorCallReply> getSuccessorCallMethod() {
    io.grpc.MethodDescriptor<chord.Chord.SuccessorCallRequest, chord.Chord.SuccessorCallReply> getSuccessorCallMethod;
    if ((getSuccessorCallMethod = NodeGrpc.getSuccessorCallMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getSuccessorCallMethod = NodeGrpc.getSuccessorCallMethod) == null) {
          NodeGrpc.getSuccessorCallMethod = getSuccessorCallMethod =
              io.grpc.MethodDescriptor.<chord.Chord.SuccessorCallRequest, chord.Chord.SuccessorCallReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "successorCall"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.SuccessorCallRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.SuccessorCallReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("successorCall"))
              .build();
        }
      }
    }
    return getSuccessorCallMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.PredecessorOfNodeRequest,
      chord.Chord.PredecessorOfNodeReqponse> getGetPredecessorOfNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getPredecessorOfNode",
      requestType = chord.Chord.PredecessorOfNodeRequest.class,
      responseType = chord.Chord.PredecessorOfNodeReqponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.PredecessorOfNodeRequest,
      chord.Chord.PredecessorOfNodeReqponse> getGetPredecessorOfNodeMethod() {
    io.grpc.MethodDescriptor<chord.Chord.PredecessorOfNodeRequest, chord.Chord.PredecessorOfNodeReqponse> getGetPredecessorOfNodeMethod;
    if ((getGetPredecessorOfNodeMethod = NodeGrpc.getGetPredecessorOfNodeMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getGetPredecessorOfNodeMethod = NodeGrpc.getGetPredecessorOfNodeMethod) == null) {
          NodeGrpc.getGetPredecessorOfNodeMethod = getGetPredecessorOfNodeMethod =
              io.grpc.MethodDescriptor.<chord.Chord.PredecessorOfNodeRequest, chord.Chord.PredecessorOfNodeReqponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getPredecessorOfNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.PredecessorOfNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.PredecessorOfNodeReqponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("getPredecessorOfNode"))
              .build();
        }
      }
    }
    return getGetPredecessorOfNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<chord.Chord.SuccessorOfNodeRequest,
      chord.Chord.SuccessorOfNodeResponse> getGetSuccessorOfNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getSuccessorOfNode",
      requestType = chord.Chord.SuccessorOfNodeRequest.class,
      responseType = chord.Chord.SuccessorOfNodeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<chord.Chord.SuccessorOfNodeRequest,
      chord.Chord.SuccessorOfNodeResponse> getGetSuccessorOfNodeMethod() {
    io.grpc.MethodDescriptor<chord.Chord.SuccessorOfNodeRequest, chord.Chord.SuccessorOfNodeResponse> getGetSuccessorOfNodeMethod;
    if ((getGetSuccessorOfNodeMethod = NodeGrpc.getGetSuccessorOfNodeMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getGetSuccessorOfNodeMethod = NodeGrpc.getGetSuccessorOfNodeMethod) == null) {
          NodeGrpc.getGetSuccessorOfNodeMethod = getGetSuccessorOfNodeMethod =
              io.grpc.MethodDescriptor.<chord.Chord.SuccessorOfNodeRequest, chord.Chord.SuccessorOfNodeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getSuccessorOfNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.SuccessorOfNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  chord.Chord.SuccessorOfNodeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("getSuccessorOfNode"))
              .build();
        }
      }
    }
    return getGetSuccessorOfNodeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NodeStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeStub>() {
        @java.lang.Override
        public NodeStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeStub(channel, callOptions);
        }
      };
    return NodeStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NodeBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeBlockingStub>() {
        @java.lang.Override
        public NodeBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeBlockingStub(channel, callOptions);
        }
      };
    return NodeBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NodeFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeFutureStub>() {
        @java.lang.Override
        public NodeFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeFutureStub(channel, callOptions);
        }
      };
    return NodeFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class NodeImplBase implements io.grpc.BindableService {

    /**
     */
    public void forwardKey(chord.Chord.ForwardKeyRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.ForwardKeyReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getForwardKeyMethod(), responseObserver);
    }

    /**
     */
    public void getNodeData(chord.Chord.NodeInfoRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.NodeInfoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNodeDataMethod(), responseObserver);
    }

    /**
     */
    public void heartBeat(chord.Chord.HeartBeatToPredecessorRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.HeartBeatReplyFromSuccessor> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHeartBeatMethod(), responseObserver);
    }

    /**
     */
    public void closestPrecedFinger(chord.Chord.ClosestPFRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.ClosestPFReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getClosestPrecedFingerMethod(), responseObserver);
    }

    /**
     */
    public void updateSuccessorPredecessor(chord.Chord.UpdateSPRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.UpdateSPResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateSuccessorPredecessorMethod(), responseObserver);
    }

    /**
     */
    public void updateFingerTable(chord.Chord.FingerTableUpdateRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.FingerTableUpdateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateFingerTableMethod(), responseObserver);
    }

    /**
     */
    public void successorCall(chord.Chord.SuccessorCallRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.SuccessorCallReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSuccessorCallMethod(), responseObserver);
    }

    /**
     */
    public void getPredecessorOfNode(chord.Chord.PredecessorOfNodeRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.PredecessorOfNodeReqponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPredecessorOfNodeMethod(), responseObserver);
    }

    /**
     */
    public void getSuccessorOfNode(chord.Chord.SuccessorOfNodeRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.SuccessorOfNodeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSuccessorOfNodeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getForwardKeyMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.ForwardKeyRequest,
                chord.Chord.ForwardKeyReply>(
                  this, METHODID_FORWARD_KEY)))
          .addMethod(
            getGetNodeDataMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.NodeInfoRequest,
                chord.Chord.NodeInfoResponse>(
                  this, METHODID_GET_NODE_DATA)))
          .addMethod(
            getHeartBeatMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.HeartBeatToPredecessorRequest,
                chord.Chord.HeartBeatReplyFromSuccessor>(
                  this, METHODID_HEART_BEAT)))
          .addMethod(
            getClosestPrecedFingerMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.ClosestPFRequest,
                chord.Chord.ClosestPFReply>(
                  this, METHODID_CLOSEST_PRECED_FINGER)))
          .addMethod(
            getUpdateSuccessorPredecessorMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.UpdateSPRequest,
                chord.Chord.UpdateSPResponse>(
                  this, METHODID_UPDATE_SUCCESSOR_PREDECESSOR)))
          .addMethod(
            getUpdateFingerTableMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.FingerTableUpdateRequest,
                chord.Chord.FingerTableUpdateResponse>(
                  this, METHODID_UPDATE_FINGER_TABLE)))
          .addMethod(
            getSuccessorCallMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.SuccessorCallRequest,
                chord.Chord.SuccessorCallReply>(
                  this, METHODID_SUCCESSOR_CALL)))
          .addMethod(
            getGetPredecessorOfNodeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.PredecessorOfNodeRequest,
                chord.Chord.PredecessorOfNodeReqponse>(
                  this, METHODID_GET_PREDECESSOR_OF_NODE)))
          .addMethod(
            getGetSuccessorOfNodeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                chord.Chord.SuccessorOfNodeRequest,
                chord.Chord.SuccessorOfNodeResponse>(
                  this, METHODID_GET_SUCCESSOR_OF_NODE)))
          .build();
    }
  }

  /**
   */
  public static final class NodeStub extends io.grpc.stub.AbstractAsyncStub<NodeStub> {
    private NodeStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeStub(channel, callOptions);
    }

    /**
     */
    public void forwardKey(chord.Chord.ForwardKeyRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.ForwardKeyReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getForwardKeyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNodeData(chord.Chord.NodeInfoRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.NodeInfoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNodeDataMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void heartBeat(chord.Chord.HeartBeatToPredecessorRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.HeartBeatReplyFromSuccessor> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHeartBeatMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closestPrecedFinger(chord.Chord.ClosestPFRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.ClosestPFReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getClosestPrecedFingerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateSuccessorPredecessor(chord.Chord.UpdateSPRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.UpdateSPResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateSuccessorPredecessorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateFingerTable(chord.Chord.FingerTableUpdateRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.FingerTableUpdateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateFingerTableMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void successorCall(chord.Chord.SuccessorCallRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.SuccessorCallReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSuccessorCallMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPredecessorOfNode(chord.Chord.PredecessorOfNodeRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.PredecessorOfNodeReqponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPredecessorOfNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSuccessorOfNode(chord.Chord.SuccessorOfNodeRequest request,
        io.grpc.stub.StreamObserver<chord.Chord.SuccessorOfNodeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSuccessorOfNodeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NodeBlockingStub extends io.grpc.stub.AbstractBlockingStub<NodeBlockingStub> {
    private NodeBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeBlockingStub(channel, callOptions);
    }

    /**
     */
    public chord.Chord.ForwardKeyReply forwardKey(chord.Chord.ForwardKeyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getForwardKeyMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.NodeInfoResponse getNodeData(chord.Chord.NodeInfoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNodeDataMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.HeartBeatReplyFromSuccessor heartBeat(chord.Chord.HeartBeatToPredecessorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHeartBeatMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.ClosestPFReply closestPrecedFinger(chord.Chord.ClosestPFRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getClosestPrecedFingerMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.UpdateSPResponse updateSuccessorPredecessor(chord.Chord.UpdateSPRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateSuccessorPredecessorMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.FingerTableUpdateResponse updateFingerTable(chord.Chord.FingerTableUpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateFingerTableMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.SuccessorCallReply successorCall(chord.Chord.SuccessorCallRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSuccessorCallMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.PredecessorOfNodeReqponse getPredecessorOfNode(chord.Chord.PredecessorOfNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPredecessorOfNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public chord.Chord.SuccessorOfNodeResponse getSuccessorOfNode(chord.Chord.SuccessorOfNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSuccessorOfNodeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NodeFutureStub extends io.grpc.stub.AbstractFutureStub<NodeFutureStub> {
    private NodeFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.ForwardKeyReply> forwardKey(
        chord.Chord.ForwardKeyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getForwardKeyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.NodeInfoResponse> getNodeData(
        chord.Chord.NodeInfoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNodeDataMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.HeartBeatReplyFromSuccessor> heartBeat(
        chord.Chord.HeartBeatToPredecessorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHeartBeatMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.ClosestPFReply> closestPrecedFinger(
        chord.Chord.ClosestPFRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getClosestPrecedFingerMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.UpdateSPResponse> updateSuccessorPredecessor(
        chord.Chord.UpdateSPRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateSuccessorPredecessorMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.FingerTableUpdateResponse> updateFingerTable(
        chord.Chord.FingerTableUpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateFingerTableMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.SuccessorCallReply> successorCall(
        chord.Chord.SuccessorCallRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSuccessorCallMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.PredecessorOfNodeReqponse> getPredecessorOfNode(
        chord.Chord.PredecessorOfNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPredecessorOfNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<chord.Chord.SuccessorOfNodeResponse> getSuccessorOfNode(
        chord.Chord.SuccessorOfNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSuccessorOfNodeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FORWARD_KEY = 0;
  private static final int METHODID_GET_NODE_DATA = 1;
  private static final int METHODID_HEART_BEAT = 2;
  private static final int METHODID_CLOSEST_PRECED_FINGER = 3;
  private static final int METHODID_UPDATE_SUCCESSOR_PREDECESSOR = 4;
  private static final int METHODID_UPDATE_FINGER_TABLE = 5;
  private static final int METHODID_SUCCESSOR_CALL = 6;
  private static final int METHODID_GET_PREDECESSOR_OF_NODE = 7;
  private static final int METHODID_GET_SUCCESSOR_OF_NODE = 8;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NodeImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NodeImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FORWARD_KEY:
          serviceImpl.forwardKey((chord.Chord.ForwardKeyRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.ForwardKeyReply>) responseObserver);
          break;
        case METHODID_GET_NODE_DATA:
          serviceImpl.getNodeData((chord.Chord.NodeInfoRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.NodeInfoResponse>) responseObserver);
          break;
        case METHODID_HEART_BEAT:
          serviceImpl.heartBeat((chord.Chord.HeartBeatToPredecessorRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.HeartBeatReplyFromSuccessor>) responseObserver);
          break;
        case METHODID_CLOSEST_PRECED_FINGER:
          serviceImpl.closestPrecedFinger((chord.Chord.ClosestPFRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.ClosestPFReply>) responseObserver);
          break;
        case METHODID_UPDATE_SUCCESSOR_PREDECESSOR:
          serviceImpl.updateSuccessorPredecessor((chord.Chord.UpdateSPRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.UpdateSPResponse>) responseObserver);
          break;
        case METHODID_UPDATE_FINGER_TABLE:
          serviceImpl.updateFingerTable((chord.Chord.FingerTableUpdateRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.FingerTableUpdateResponse>) responseObserver);
          break;
        case METHODID_SUCCESSOR_CALL:
          serviceImpl.successorCall((chord.Chord.SuccessorCallRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.SuccessorCallReply>) responseObserver);
          break;
        case METHODID_GET_PREDECESSOR_OF_NODE:
          serviceImpl.getPredecessorOfNode((chord.Chord.PredecessorOfNodeRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.PredecessorOfNodeReqponse>) responseObserver);
          break;
        case METHODID_GET_SUCCESSOR_OF_NODE:
          serviceImpl.getSuccessorOfNode((chord.Chord.SuccessorOfNodeRequest) request,
              (io.grpc.stub.StreamObserver<chord.Chord.SuccessorOfNodeResponse>) responseObserver);
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

  private static abstract class NodeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NodeBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return chord.Chord.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Node");
    }
  }

  private static final class NodeFileDescriptorSupplier
      extends NodeBaseDescriptorSupplier {
    NodeFileDescriptorSupplier() {}
  }

  private static final class NodeMethodDescriptorSupplier
      extends NodeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NodeMethodDescriptorSupplier(String methodName) {
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
      synchronized (NodeGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NodeFileDescriptorSupplier())
              .addMethod(getForwardKeyMethod())
              .addMethod(getGetNodeDataMethod())
              .addMethod(getHeartBeatMethod())
              .addMethod(getClosestPrecedFingerMethod())
              .addMethod(getUpdateSuccessorPredecessorMethod())
              .addMethod(getUpdateFingerTableMethod())
              .addMethod(getSuccessorCallMethod())
              .addMethod(getGetPredecessorOfNodeMethod())
              .addMethod(getGetSuccessorOfNodeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
