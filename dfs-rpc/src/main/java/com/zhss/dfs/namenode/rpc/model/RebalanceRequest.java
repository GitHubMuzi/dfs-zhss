// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NameNodeRpcModel.proto

package com.zhss.dfs.namenode.rpc.model;

/**
 * Protobuf type {@code com.zhss.dfs.namenode.rpc.RebalanceRequest}
 */
public  final class RebalanceRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.zhss.dfs.namenode.rpc.RebalanceRequest)
    RebalanceRequestOrBuilder {
  // Use RebalanceRequest.newBuilder() to construct.
  private RebalanceRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RebalanceRequest() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private RebalanceRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_RebalanceRequest_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_RebalanceRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zhss.dfs.namenode.rpc.model.RebalanceRequest.class, com.zhss.dfs.namenode.rpc.model.RebalanceRequest.Builder.class);
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.zhss.dfs.namenode.rpc.model.RebalanceRequest)) {
      return super.equals(obj);
    }
    com.zhss.dfs.namenode.rpc.model.RebalanceRequest other = (com.zhss.dfs.namenode.rpc.model.RebalanceRequest) obj;

    boolean result = true;
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.zhss.dfs.namenode.rpc.model.RebalanceRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.zhss.dfs.namenode.rpc.RebalanceRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.zhss.dfs.namenode.rpc.RebalanceRequest)
      com.zhss.dfs.namenode.rpc.model.RebalanceRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_RebalanceRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_RebalanceRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zhss.dfs.namenode.rpc.model.RebalanceRequest.class, com.zhss.dfs.namenode.rpc.model.RebalanceRequest.Builder.class);
    }

    // Construct using com.zhss.dfs.namenode.rpc.model.RebalanceRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_RebalanceRequest_descriptor;
    }

    public com.zhss.dfs.namenode.rpc.model.RebalanceRequest getDefaultInstanceForType() {
      return com.zhss.dfs.namenode.rpc.model.RebalanceRequest.getDefaultInstance();
    }

    public com.zhss.dfs.namenode.rpc.model.RebalanceRequest build() {
      com.zhss.dfs.namenode.rpc.model.RebalanceRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.zhss.dfs.namenode.rpc.model.RebalanceRequest buildPartial() {
      com.zhss.dfs.namenode.rpc.model.RebalanceRequest result = new com.zhss.dfs.namenode.rpc.model.RebalanceRequest(this);
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.zhss.dfs.namenode.rpc.model.RebalanceRequest) {
        return mergeFrom((com.zhss.dfs.namenode.rpc.model.RebalanceRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zhss.dfs.namenode.rpc.model.RebalanceRequest other) {
      if (other == com.zhss.dfs.namenode.rpc.model.RebalanceRequest.getDefaultInstance()) return this;
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.zhss.dfs.namenode.rpc.model.RebalanceRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.zhss.dfs.namenode.rpc.model.RebalanceRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:com.zhss.dfs.namenode.rpc.RebalanceRequest)
  }

  // @@protoc_insertion_point(class_scope:com.zhss.dfs.namenode.rpc.RebalanceRequest)
  private static final com.zhss.dfs.namenode.rpc.model.RebalanceRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zhss.dfs.namenode.rpc.model.RebalanceRequest();
  }

  public static com.zhss.dfs.namenode.rpc.model.RebalanceRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RebalanceRequest>
      PARSER = new com.google.protobuf.AbstractParser<RebalanceRequest>() {
    public RebalanceRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new RebalanceRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RebalanceRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RebalanceRequest> getParserForType() {
    return PARSER;
  }

  public com.zhss.dfs.namenode.rpc.model.RebalanceRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

