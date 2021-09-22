package com.xueyi.common.socket.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigureConfig {

    @Value("${server.value:/}")
    public String value;

    @Value("${socket.path:/}")
    public String path;

    @Value("${socket.host:0.0.0.0}")
    public String host;

    @Value("${server.port}")
    public String port;

    @Value("${socket.bossGroup:1}")
    public String bossGroup;

    @Value("${socket.workGroup:0}")
    public String workGroup;

    @Value("${socket.useCompressionHandler:false}")
    public String useCompressionHandler;

    /** ------------------------- option ------------------------- */

    @Value("${socket.option.optionConnectTimeoutMillis:30000}")
    public String connectTimeoutMillis;

    @Value("${socket.option.optionSoBacklog:128}")
    public String soBacklog;

    /** ------------------------- childOption ------------------------- */

    @Value("${socket.child.writeSpinCount:16}")
    public String writeSpinCount;

    @Value("${socket.child.writeBufferHighWaterMark:65536}")
    public String writeBufferHighWaterMark;

    @Value("${socket.child.writeBufferLowWaterMark:32768}")
    public String writeBufferLowWaterMark;

    @Value("${socket.child.soRcvbuf:-1}")
    public String soRcvbuf;

    @Value("${socket.child.soSndbuf:-1}")
    public String soSndbuf;

    @Value("${socket.child.tcpNodeLay:true}")
    public String tcpNodeLay;

    @Value("${socket.child.soKeepalive:false}")
    public String soKeepalive;

    @Value("${socket.child.soLinger:-1}")
    public String soLinger;

    @Value("${socket.child.allowHalfClosure:false}")
    public String allowHalfClosure;

    /** ------------------------- idleEvent ------------------------- */

    @Value("${socket.idleEvent.readerIdleTimeSeconds:0}")
    public String readerIdleTimeSeconds;

    @Value("${socket.idleEvent.writerIdleTimeSeconds:0}")
    public String writerIdleTimeSeconds;

    @Value("${socket.idleEvent.allIdleTimeSeconds:0}")
    public String allIdleTimeSeconds;

    /** ------------------------- handshake ------------------------- */

    @Value("${socket.handshake.maxFramePayloadLength:65536}")
    public String maxFramePayloadLength;

    /** ------------------------- eventExecutorGroup ------------------------- */

    @Value("${socket.eventExecutorGroup.useEventExecutorGroup:true}")
    public String useEventExecutorGroup;

    @Value("${socket.eventExecutorGroup.eventExecutorGroupThreads:16}")
    public String eventExecutorGroupThreads;

    /** ------------------------- ssl (refer to spring ssl) ------------------------- */

    @Value("${socket.ssl.sslKeyPassword:}")
    public String sslKeyPassword;

    @Value("${socket.ssl.sslKeyStore:}")
    public String sslKeyStore;

    @Value("${socket.ssl.sslKeyStorePassword:}")
    public String sslKeyStorePassword;

    @Value("${socket.ssl.sslKeyStoreType:}")
    public String sslKeyStoreType;

    @Value("${socket.ssl.sslTrustStore:}")
    public String sslTrustStore;

    @Value("${socket.ssl.sslTrustStorePassword:}")
    public  String sslTrustStorePassword;

    @Value("${socket.ssl.sslTrustStoreType:}")
    public String sslTrustStoreType;

    /** ------------------------- cors (refer to spring CrossOrigin) ------------------------- */

    @Value("${socket.cors.corsOrigins:#{{}}}")
    public String[] corsOrigins;

    @Value("${socket.cors.corsAllowCredentials:}")
    public String corsAllowCredentials;

    public String getValue(String value) {
        return StringUtils.isNotBlank(value) ? value : this.value;
    }

    public String getPath(String path) {
        return StringUtils.isNotBlank(path) ? path : this.path;
    }

    public String getHost(String host) {
        return StringUtils.isNotBlank(host) ? host : this.host;
    }

    public String getPort(String port) {
        return StringUtils.isNotBlank(port) ? port : this.port;
    }

    public String getBossGroup(String bossGroup) {
        return StringUtils.isNotBlank(bossGroup) ? bossGroup : this.bossGroup;
    }

    public String getWorkGroup(String workGroup) {
        return StringUtils.isNotBlank(workGroup) ? workGroup : this.workGroup;
    }

    public String getUseCompressionHandler(String useCompressionHandler) {
        return StringUtils.isNotBlank(useCompressionHandler) ? useCompressionHandler : this.useCompressionHandler;
    }

    public String getConnectTimeoutMillis(String connectTimeoutMillis) {
        return StringUtils.isNotBlank(connectTimeoutMillis) ? connectTimeoutMillis : this.connectTimeoutMillis;
    }

    public String getSoBacklog(String soBacklog) {
        return StringUtils.isNotBlank(soBacklog) ? soBacklog : this.soBacklog;
    }

    public String getWriteSpinCount(String writeSpinCount) {
        return StringUtils.isNotBlank(writeSpinCount) ? writeSpinCount : this.writeSpinCount;
    }

    public String getWriteBufferHighWaterMark(String writeBufferHighWaterMark) {
        return StringUtils.isNotBlank(writeBufferHighWaterMark) ? writeBufferHighWaterMark : this.writeBufferHighWaterMark;
    }

    public String getWriteBufferLowWaterMark(String writeBufferLowWaterMark) {
        return StringUtils.isNotBlank(writeBufferLowWaterMark) ? writeBufferLowWaterMark : this.writeBufferLowWaterMark;
    }

    public String getSoRcvbuf(String soRcvbuf) {
        return StringUtils.isNotBlank(soRcvbuf) ? soRcvbuf : this.soRcvbuf;
    }

    public String getSoSndbuf(String soSndbuf) {
        return StringUtils.isNotBlank(soSndbuf) ? soSndbuf : this.soSndbuf;
    }

    public String getTcpNodeLay(String tcpNodeLay) {
        return StringUtils.isNotBlank(tcpNodeLay) ? tcpNodeLay : this.tcpNodeLay;
    }

    public String getSoKeepalive(String soKeepalive) {
        return StringUtils.isNotBlank(soKeepalive) ? soKeepalive : this.soKeepalive;
    }

    public String getSoLinger(String soLinger) {
        return StringUtils.isNotBlank(soLinger) ? soLinger : this.soLinger;
    }

    public String getAllowHalfClosure(String allowHalfClosure) {
        return StringUtils.isNotBlank(allowHalfClosure) ? allowHalfClosure : this.allowHalfClosure;
    }

    public String getReaderIdleTimeSeconds(String readerIdleTimeSeconds) {
        return StringUtils.isNotBlank(readerIdleTimeSeconds) ? readerIdleTimeSeconds : this.readerIdleTimeSeconds;
    }

    public String getWriterIdleTimeSeconds(String writerIdleTimeSeconds) {
        return StringUtils.isNotBlank(writerIdleTimeSeconds) ? writerIdleTimeSeconds : this.writerIdleTimeSeconds;
    }

    public String getAllIdleTimeSeconds(String allIdleTimeSeconds) {
        return StringUtils.isNotBlank(allIdleTimeSeconds) ? allIdleTimeSeconds : this.allIdleTimeSeconds;
    }

    public String getMaxFramePayloadLength(String maxFramePayloadLength) {
        return StringUtils.isNotBlank(maxFramePayloadLength) ? maxFramePayloadLength : this.maxFramePayloadLength;
    }

    public String getUseEventExecutorGroup(String useEventExecutorGroup) {
        return StringUtils.isNotBlank(useEventExecutorGroup) ? useEventExecutorGroup : this.useEventExecutorGroup;
    }

    public String getEventExecutorGroupThreads(String eventExecutorGroupThreads) {
        return StringUtils.isNotBlank(eventExecutorGroupThreads) ? eventExecutorGroupThreads : this.eventExecutorGroupThreads;
    }

    public String getSslKeyPassword(String sslKeyPassword) {
        return StringUtils.isNotBlank(sslKeyPassword) ? sslKeyPassword : this.sslKeyPassword;
    }

    public String getSslKeyStore(String sslKeyStore) {
        return StringUtils.isNotBlank(sslKeyStore) ? sslKeyStore : this.sslKeyStore;
    }

    public String getSslKeyStorePassword(String sslKeyStorePassword) {
        return StringUtils.isNotBlank(sslKeyStorePassword) ? sslKeyStorePassword : this.sslKeyStorePassword;
    }

    public String getSslKeyStoreType(String sslKeyStoreType) {
        return StringUtils.isNotBlank(sslKeyStoreType) ? sslKeyStoreType : this.sslKeyStoreType;
    }

    public String getSslTrustStore(String sslTrustStore) {
        return StringUtils.isNotBlank(sslTrustStore) ? sslTrustStore : this.sslTrustStore;
    }

    public String getSslTrustStorePassword(String sslTrustStorePassword) {
        return StringUtils.isNotBlank(sslTrustStorePassword) ? sslTrustStorePassword : this.sslTrustStorePassword;
    }

    public String getSslTrustStoreType(String sslTrustStoreType) {
        return StringUtils.isNotBlank(sslTrustStoreType) ? sslTrustStoreType : this.sslTrustStoreType;
    }

    public String[] getCorsOrigins(String[] corsOrigins) {
        return corsOrigins.length>0 ? corsOrigins : this.corsOrigins;
    }

    public String getCorsAllowCredentials(String corsAllowCredentials) {
        return StringUtils.isNotBlank(corsAllowCredentials) ? corsAllowCredentials : this.corsAllowCredentials;
    }
}