package com.xueyi.common.socket.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerEndpoint {

    @AliasFor("path")
    String value() default "";

    @AliasFor("value")
    String path() default "";

    String host() default "";

    String port() default "";

    String bossGroup() default "";

    String workGroup() default "";

    String useCompressionHandler() default "";

    String connectTimeoutMillis() default "";

    String soBacklog() default "";

    String writeSpinCount() default "";

    String writeBufferHighWaterMark() default "";

    String writeBufferLowWaterMark() default "";

    String soRcvbuf() default "";

    String soSndbuf() default "";

    String tcpNodeLay() default "";

    String soKeepalive() default "";

    String soLinger() default "";

    String allowHalfClosure() default "";

    String readerIdleTimeSeconds() default "";

    String writerIdleTimeSeconds() default "";

    String allIdleTimeSeconds() default "";

    String maxFramePayloadLength() default "";

    String useEventExecutorGroup() default "";

    String eventExecutorGroupThreads() default "";

    String sslKeyPassword() default "";

    String sslKeyStore() default "";

    String sslKeyStorePassword() default "";

    String sslKeyStoreType() default "";

    String sslTrustStore() default "";

    String sslTrustStorePassword() default "";

    String sslTrustStoreType() default "";

    String[] corsOrigins() default {};

    String corsAllowCredentials() default "";
}