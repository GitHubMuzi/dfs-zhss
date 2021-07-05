// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NameNodeRpcModel.proto

package com.zhss.dfs.namenode.rpc.model;

/**
 * Protobuf type {@code com.zhss.dfs.namenode.rpc.ReportCompleteStorageInfoResponse}
 */
public  final class ReportCompleteStorageInfoResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.zhss.dfs.namenode.rpc.ReportCompleteStorageInfoResponse)
    ReportCompleteStorageInfoResponseOrBuilder {
  // Use ReportCompleteStorageInfoResponse.newBuilder() to construct.
  private ReportCompleteStorageInfoResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ReportCompleteStorageInfoResponse() {
    status_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private ReportCompleteStorageInfoResponse(
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

            status_ = input.readInt32();
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
    return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ReportCompleteStorageInfoResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ReportCompleteStorageInfoResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse.class, com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse.Builder.class);
  }

  public static final int STATUS_FIELD_NUMBER = 1;
  private int status_;
  /**
   * <code>optional int32 status = 1;</code>
   */
  public int getStatus() {
    return status_;
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
    if (status_ != 0) {
      output.writeInt32(1, status_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (status_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, status_);
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
    if (!(obj instanceof com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse)) {
      return super.equals(obj);
    }
    com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse other = (com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse) obj;

    boolean result = true;
    result = result && (getStatus()
        == other.getStatus());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (37 * hash) + STATUS_FIELD_NUMBER;
    hash = (53 * hash) + getStatus();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parseFrom(
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
  public static Builder newBuilder(com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse prototype) {
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
   * Protobuf type {@code com.zhss.dfs.namenode.rpc.ReportCompleteStorageInfoResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.zhss.dfs.namenode.rpc.ReportCompleteStorageInfoResponse)
      com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ReportCompleteStorageInfoResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ReportCompleteStorageInfoResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse.class, com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse.Builder.class);
    }

    // Construct using com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse.newBuilder()
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
      status_ = 0;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zhss.dfs.namenode.rpc.model.NameNodeRpcModel.internal_static_com_zhss_dfs_namenode_rpc_ReportCompleteStorageInfoResponse_descriptor;
    }

    public com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse getDefaultInstanceForType() {
      return com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse.getDefaultInstance();
    }

    public com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse build() {
      com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse buildPartial() {
      com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse result = new com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse(this);
      result.status_ = status_;
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
      if (other instanceof com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse) {
        return mergeFrom((com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse other) {
      if (other == com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse.getDefaultInstance()) return this;
      if (other.getStatus() != 0) {
        setStatus(other.getStatus());
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
      com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int status_ ;
    /**
     * <code>optional int32 status = 1;</code>
     */
    public int getStatus() {
      return status_;
    }
    /**
     * <code>optional int32 status = 1;</code>
     */
    public Builder setStatus(int value) {
      
      status_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 status = 1;</code>
     */
    public Builder clearStatus() {
      
      status_ = 0;
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


    // @@protoc_insertion_point(builder_scope:com.zhss.dfs.namenode.rpc.ReportCompleteStorageInfoResponse)
  }

  // @@protoc_insertion_point(class_scope:com.zhss.dfs.namenode.rpc.ReportCompleteStorageInfoResponse)
  private static final com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse();
  }

  public static com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ReportCompleteStorageInfoResponse>
      PARSER = new com.google.protobuf.AbstractParser<ReportCompleteStorageInfoResponse>() {
    public ReportCompleteStorageInfoResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new ReportCompleteStorageInfoResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ReportCompleteStorageInfoResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ReportCompleteStorageInfoResponse> getParserForType() {
    return PARSER;
  }

  public com.zhss.dfs.namenode.rpc.model.ReportCompleteStorageInfoResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

