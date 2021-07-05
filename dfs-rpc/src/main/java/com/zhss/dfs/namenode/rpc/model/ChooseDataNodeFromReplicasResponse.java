// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NameNodeRpcModel.proto

package com.zhss.dfs.namenode.rpc.model;

/**
 * Protobuf type {@code com.zhss.dfs.namenode.rpc.ChooseDataNodeFromReplicasResponse}
 */
public  final class ChooseDataNodeFromReplicasResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.zhss.dfs.namenode.rpc.ChooseDataNodeFromReplicasResponse)
    ChooseDataNodeFromReplicasResponseOrBuilder {
  // Use ChooseDataNodeFromReplicasResponse.newBuilder() to construct.
  private ChooseDataNodeFromReplicasResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ChooseDataNodeFromReplicasResponse() {
    datanode_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private ChooseDataNodeFromReplicasResponse(
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
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            datanode_ = s;
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
    return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ChooseDataNodeFromReplicasResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ChooseDataNodeFromReplicasResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse.class, com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse.Builder.class);
  }

  public static final int DATANODE_FIELD_NUMBER = 1;
  private volatile java.lang.Object datanode_;
  /**
   * <code>optional string datanode = 1;</code>
   */
  public java.lang.String getDatanode() {
    java.lang.Object ref = datanode_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      datanode_ = s;
      return s;
    }
  }
  /**
   * <code>optional string datanode = 1;</code>
   */
  public com.google.protobuf.ByteString
      getDatanodeBytes() {
    java.lang.Object ref = datanode_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      datanode_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    if (!getDatanodeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, datanode_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getDatanodeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, datanode_);
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
    if (!(obj instanceof com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse)) {
      return super.equals(obj);
    }
    com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse other = (com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse) obj;

    boolean result = true;
    result = result && getDatanode()
        .equals(other.getDatanode());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (37 * hash) + DATANODE_FIELD_NUMBER;
    hash = (53 * hash) + getDatanode().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parseFrom(
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
  public static Builder newBuilder(com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse prototype) {
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
   * Protobuf type {@code com.zhss.dfs.namenode.rpc.ChooseDataNodeFromReplicasResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.zhss.dfs.namenode.rpc.ChooseDataNodeFromReplicasResponse)
      com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ChooseDataNodeFromReplicasResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ChooseDataNodeFromReplicasResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse.class, com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse.Builder.class);
    }

    // Construct using com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse.newBuilder()
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
      datanode_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ChooseDataNodeFromReplicasResponse_descriptor;
    }

    public com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse getDefaultInstanceForType() {
      return com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse.getDefaultInstance();
    }

    public com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse build() {
      com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse buildPartial() {
      com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse result = new com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse(this);
      result.datanode_ = datanode_;
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
      if (other instanceof com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse) {
        return mergeFrom((com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse other) {
      if (other == com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse.getDefaultInstance()) return this;
      if (!other.getDatanode().isEmpty()) {
        datanode_ = other.datanode_;
        onChanged();
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
      com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object datanode_ = "";
    /**
     * <code>optional string datanode = 1;</code>
     */
    public java.lang.String getDatanode() {
      java.lang.Object ref = datanode_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        datanode_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string datanode = 1;</code>
     */
    public com.google.protobuf.ByteString
        getDatanodeBytes() {
      java.lang.Object ref = datanode_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        datanode_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string datanode = 1;</code>
     */
    public Builder setDatanode(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      datanode_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string datanode = 1;</code>
     */
    public Builder clearDatanode() {
      
      datanode_ = getDefaultInstance().getDatanode();
      onChanged();
      return this;
    }
    /**
     * <code>optional string datanode = 1;</code>
     */
    public Builder setDatanodeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      datanode_ = value;
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


    // @@protoc_insertion_point(builder_scope:com.zhss.dfs.namenode.rpc.ChooseDataNodeFromReplicasResponse)
  }

  // @@protoc_insertion_point(class_scope:com.zhss.dfs.namenode.rpc.ChooseDataNodeFromReplicasResponse)
  private static final com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse();
  }

  public static com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ChooseDataNodeFromReplicasResponse>
      PARSER = new com.google.protobuf.AbstractParser<ChooseDataNodeFromReplicasResponse>() {
    public ChooseDataNodeFromReplicasResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new ChooseDataNodeFromReplicasResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ChooseDataNodeFromReplicasResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ChooseDataNodeFromReplicasResponse> getParserForType() {
    return PARSER;
  }

  public com.zhss.dfs.namenode.rpc.model.ChooseDataNodeFromReplicasResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

