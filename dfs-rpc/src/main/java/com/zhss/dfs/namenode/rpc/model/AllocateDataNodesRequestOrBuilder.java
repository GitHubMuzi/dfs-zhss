// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NameNodeRpcModel.proto

package com.zhss.dfs.namenode.rpc.model;

public interface AllocateDataNodesRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.zhss.dfs.namenode.rpc.AllocateDataNodesRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string filename = 1;</code>
   */
  java.lang.String getFilename();
  /**
   * <code>optional string filename = 1;</code>
   */
  com.google.protobuf.ByteString
      getFilenameBytes();

  /**
   * <code>optional int64 fileSize = 2;</code>
   */
  long getFileSize();
}