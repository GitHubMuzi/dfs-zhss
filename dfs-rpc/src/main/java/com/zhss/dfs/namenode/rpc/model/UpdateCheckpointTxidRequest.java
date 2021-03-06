// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NameNodeRpcModel.proto

package com.zhss.dfs.namenode.rpc.model;

/**
 * Protobuf type {@code com.zhss.dfs.namenode.rpc.UpdateCheckpointTxidRequest}
 */
public  final class UpdateCheckpointTxidRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.zhss.dfs.namenode.rpc.UpdateCheckpointTxidRequest)
    UpdateCheckpointTxidRequestOrBuilder {
  // Use UpdateCheckpointTxidRequest.newBuilder() to construct.
  private UpdateCheckpointTxidRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private UpdateCheckpointTxidRequest() {
    txid_ = 0L;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private UpdateCheckpointTxidRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
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
          case 8: {

            txid_ = input.readInt64();
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
    return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_UpdateCheckpointTxidRequest_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_UpdateCheckpointTxidRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest.class, com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest.Builder.class);
  }

  public static final int TXID_FIELD_NUMBER = 1;
  private long txid_;
  /**
   * <code>optional int64 txid = 1;</code>
   */
  public long getTxid() {
    return txid_;
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
    if (txid_ != 0L) {
      output.writeInt64(1, txid_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (txid_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, txid_);
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest)) {
      return super.equals(obj);
    }
    com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest other = (com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest) obj;

    boolean result = true;
    result = result && (getTxid()
        == other.getTxid());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (37 * hash) + TXID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTxid());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parseFrom(
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
  public static Builder newBuilder(com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest prototype) {
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
   * Protobuf type {@code com.zhss.dfs.namenode.rpc.UpdateCheckpointTxidRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.zhss.dfs.namenode.rpc.UpdateCheckpointTxidRequest)
      com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_UpdateCheckpointTxidRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_UpdateCheckpointTxidRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest.class, com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest.Builder.class);
    }

    // Construct using com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest.newBuilder()
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
      txid_ = 0L;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_UpdateCheckpointTxidRequest_descriptor;
    }

    public com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest getDefaultInstanceForType() {
      return com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest.getDefaultInstance();
    }

    public com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest build() {
      com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest buildPartial() {
      com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest result = new com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest(this);
      result.txid_ = txid_;
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
      if (other instanceof com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest) {
        return mergeFrom((com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest other) {
      if (other == com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest.getDefaultInstance()) return this;
      if (other.getTxid() != 0L) {
        setTxid(other.getTxid());
      }
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
      com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private long txid_ ;
    /**
     * <code>optional int64 txid = 1;</code>
     */
    public long getTxid() {
      return txid_;
    }
    /**
     * <code>optional int64 txid = 1;</code>
     */
    public Builder setTxid(long value) {
      
      txid_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int64 txid = 1;</code>
     */
    public Builder clearTxid() {
      
      txid_ = 0L;
      onChanged();
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


    // @@protoc_insertion_point(builder_scope:com.zhss.dfs.namenode.rpc.UpdateCheckpointTxidRequest)
  }

  // @@protoc_insertion_point(class_scope:com.zhss.dfs.namenode.rpc.UpdateCheckpointTxidRequest)
  private static final com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest();
  }

  public static com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<UpdateCheckpointTxidRequest>
      PARSER = new com.google.protobuf.AbstractParser<UpdateCheckpointTxidRequest>() {
    public UpdateCheckpointTxidRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new UpdateCheckpointTxidRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<UpdateCheckpointTxidRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<UpdateCheckpointTxidRequest> getParserForType() {
    return PARSER;
  }

  public com.zhss.dfs.namenode.rpc.model.UpdateCheckpointTxidRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

