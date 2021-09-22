package com.xueyi.common.socket.standard;

import com.xueyi.common.socket.annotation.EnableWebSocket;
import com.xueyi.common.socket.annotation.ServerEndpoint;
import com.xueyi.common.socket.config.ConfigureConfig;
import com.xueyi.common.socket.exception.DeploymentException;
import com.xueyi.common.socket.pojo.PojoEndpointServer;
import com.xueyi.common.socket.pojo.PojoMethodMapping;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * @author Yeauty
 */
public class ServerEndpointExporter extends ApplicationObjectSupport implements SmartInitializingSingleton, BeanFactoryAware, ResourceLoaderAware {

    @Autowired
    Environment environment;

    @Autowired
    private ConfigureConfig configureConfig;

    private AbstractBeanFactory beanFactory;

    private ResourceLoader resourceLoader;

    private final Map<InetSocketAddress, WebsocketServer> addressWebsocketServerMap = new HashMap<>();

    @Override
    public void afterSingletonsInstantiated() {
        registerEndpoints();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof AbstractBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a AbstractBeanFactory: " + beanFactory);
        }
        this.beanFactory = (AbstractBeanFactory) beanFactory;
    }

    protected void registerEndpoints() {
        ApplicationContext context = getApplicationContext();

        scanPackage(context);

        String[] endpointBeanNames = context.getBeanNamesForAnnotation(ServerEndpoint.class);
        Set<Class<?>> endpointClasses = new LinkedHashSet<>();
        for (String beanName : endpointBeanNames) {
            endpointClasses.add(context.getType(beanName));
        }

        for (Class<?> endpointClass : endpointClasses) {
            if (ClassUtils.isCglibProxyClass(endpointClass)) {
                registerEndpoint(endpointClass.getSuperclass());
            } else {
                registerEndpoint(endpointClass);
            }
        }

        init();
    }

    private void scanPackage(ApplicationContext context) {
        String[] basePackages = null;

        String[] enableWebSocketBeanNames = context.getBeanNamesForAnnotation(EnableWebSocket.class);
        if (enableWebSocketBeanNames.length != 0) {
            for (String enableWebSocketBeanName : enableWebSocketBeanNames) {
                Object enableWebSocketBean = context.getBean(enableWebSocketBeanName);
                EnableWebSocket enableWebSocket = AnnotationUtils.findAnnotation(enableWebSocketBean.getClass(), EnableWebSocket.class);
                assert enableWebSocket != null;
                if (enableWebSocket.scanBasePackages().length != 0) {
                    basePackages = enableWebSocket.scanBasePackages();
                    break;
                }
            }
        }

        // use @SpringBootApplication package
        if (basePackages == null) {
            String[] springBootApplicationBeanName = context.getBeanNamesForAnnotation(SpringBootApplication.class);
            Object springBootApplicationBean = context.getBean(springBootApplicationBeanName[0]);
            SpringBootApplication springBootApplication = AnnotationUtils.findAnnotation(springBootApplicationBean.getClass(), SpringBootApplication.class);
            assert springBootApplication != null;
            if (springBootApplication.scanBasePackages().length != 0) {
                basePackages = springBootApplication.scanBasePackages();
            } else {
                String packageName = ClassUtils.getPackageName(springBootApplicationBean.getClass().getName());
                basePackages = new String[1];
                basePackages[0] = packageName;
            }
        }

        EndpointClassPathScanner scanHandle = new EndpointClassPathScanner((BeanDefinitionRegistry) context, false);
        if (resourceLoader != null) {
            scanHandle.setResourceLoader(resourceLoader);
        }

        for (String basePackage : basePackages) {
            scanHandle.doScan(basePackage);
        }
    }

    private void init() {
        for (Map.Entry<InetSocketAddress, WebsocketServer> entry : addressWebsocketServerMap.entrySet()) {
            WebsocketServer websocketServer = entry.getValue();
            try {
                websocketServer.init();
                PojoEndpointServer pojoEndpointServer = websocketServer.getPojoEndpointServer();
                StringJoiner stringJoiner = new StringJoiner(",");
                pojoEndpointServer.getPathMatcherSet().forEach(pathMatcher -> stringJoiner.add("'" + pathMatcher.getPattern() + "'"));
                logger.info(String.format("\033[34mNetty WebSocket started on port: %s with context path(s): %s .\033[0m", pojoEndpointServer.getPort(), stringJoiner.toString()));
            } catch (InterruptedException e) {
                logger.error(String.format("websocket [%s] init fail", entry.getKey()), e);
            } catch (SSLException e) {
                logger.error(String.format("websocket [%s] ssl create fail", entry.getKey()), e);

            }
        }
    }

    private void registerEndpoint(Class<?> endpointClass) {
        ServerEndpoint annotation = AnnotatedElementUtils.findMergedAnnotation(endpointClass, ServerEndpoint.class);
        if (annotation == null) {
            throw new IllegalStateException("missingAnnotation ServerEndpoint");
        }
        ServerEndpointConfig serverEndpointConfig = buildConfig(annotation);

        ApplicationContext context = getApplicationContext();
        PojoMethodMapping pojoMethodMapping = null;
        try {
            pojoMethodMapping = new PojoMethodMapping(endpointClass, context, beanFactory);
        } catch (DeploymentException e) {
            throw new IllegalStateException("Failed to register ServerEndpointConfig: " + serverEndpointConfig, e);
        }

        InetSocketAddress inetSocketAddress = new InetSocketAddress(serverEndpointConfig.getHost(), serverEndpointConfig.getPort());
        String path = resolveAnnotationValue(annotation.value(), String.class, "path");

        WebsocketServer websocketServer = addressWebsocketServerMap.get(inetSocketAddress);
        if (websocketServer == null) {
            PojoEndpointServer pojoEndpointServer = new PojoEndpointServer(pojoMethodMapping, serverEndpointConfig, path);
            websocketServer = new WebsocketServer(pojoEndpointServer, serverEndpointConfig);
            addressWebsocketServerMap.put(inetSocketAddress, websocketServer);
        } else {
            websocketServer.getPojoEndpointServer().addPathPojoMethodMapping(path, pojoMethodMapping);
        }
    }

    @Value("${server.port:/}")
    public String hostss;

    private ServerEndpointConfig buildConfig(ServerEndpoint annotation) {

        String path = resolveAnnotationValue(configureConfig.getValue(annotation.value()), String.class, "value");
        String host = resolveAnnotationValue(configureConfig.getHost(annotation.host()), String.class, "host");
        int port = resolveAnnotationValue(configureConfig.getPort(annotation.port()), Integer.class, "port");
        int bossLoopGroupThreads = resolveAnnotationValue(configureConfig.getBossGroup(annotation.bossGroup()), Integer.class, "bossLoopGroupThreads");
        int workerLoopGroupThreads = resolveAnnotationValue(configureConfig.getWorkGroup(annotation.workGroup()), Integer.class, "workerLoopGroupThreads");
        boolean useCompressionHandler = resolveAnnotationValue(configureConfig.getUseCompressionHandler(annotation.useCompressionHandler()), Boolean.class, "useCompressionHandler");

        int optionConnectTimeoutMillis = resolveAnnotationValue(configureConfig.getConnectTimeoutMillis(annotation.connectTimeoutMillis()), Integer.class, "optionConnectTimeoutMillis");
        int optionSoBacklog = resolveAnnotationValue(configureConfig.getSoBacklog(annotation.soBacklog()), Integer.class, "optionSoBacklog");

        int childOptionWriteSpinCount = resolveAnnotationValue(configureConfig.getWriteSpinCount(annotation.writeSpinCount()), Integer.class, "childOptionWriteSpinCount");
        int childOptionWriteBufferHighWaterMark = resolveAnnotationValue(configureConfig.getWriteBufferHighWaterMark(annotation.writeBufferHighWaterMark()), Integer.class, "childOptionWriteBufferHighWaterMark");
        int childOptionWriteBufferLowWaterMark = resolveAnnotationValue(configureConfig.getWriteBufferLowWaterMark(annotation.writeBufferLowWaterMark()), Integer.class, "childOptionWriteBufferLowWaterMark");
        int childOptionSoRcvbuf = resolveAnnotationValue(configureConfig.getSoRcvbuf(annotation.soRcvbuf()), Integer.class, "childOptionSoRcvbuf");
        int childOptionSoSndbuf = resolveAnnotationValue(configureConfig.getSoSndbuf(annotation.soSndbuf()), Integer.class, "childOptionSoSndbuf");
        boolean childOptionTcpNodelay = resolveAnnotationValue(configureConfig.getTcpNodeLay(annotation.tcpNodeLay()), Boolean.class, "childOptionTcpNodelay");
        boolean childOptionSoKeepalive = resolveAnnotationValue(configureConfig.getSoKeepalive(annotation.soKeepalive()), Boolean.class, "childOptionSoKeepalive");
        int childOptionSoLinger = resolveAnnotationValue(configureConfig.getSoLinger(annotation.soLinger()), Integer.class, "childOptionSoLinger");
        boolean childOptionAllowHalfClosure = resolveAnnotationValue(configureConfig.getAllowHalfClosure(annotation.allowHalfClosure()), Boolean.class, "childOptionAllowHalfClosure");

        int readerIdleTimeSeconds = resolveAnnotationValue(configureConfig.getReaderIdleTimeSeconds(annotation.readerIdleTimeSeconds()), Integer.class, "readerIdleTimeSeconds");
        int writerIdleTimeSeconds = resolveAnnotationValue(configureConfig.getWriterIdleTimeSeconds(annotation.writerIdleTimeSeconds()), Integer.class, "writerIdleTimeSeconds");
        int allIdleTimeSeconds = resolveAnnotationValue(configureConfig.getAllIdleTimeSeconds(annotation.allIdleTimeSeconds()), Integer.class, "allIdleTimeSeconds");

        int maxFramePayloadLength = resolveAnnotationValue(configureConfig.getMaxFramePayloadLength(annotation.maxFramePayloadLength()), Integer.class, "maxFramePayloadLength");

        boolean useEventExecutorGroup = resolveAnnotationValue(configureConfig.getUseEventExecutorGroup(annotation.useEventExecutorGroup()), Boolean.class, "useEventExecutorGroup");
        int eventExecutorGroupThreads = resolveAnnotationValue(configureConfig.getEventExecutorGroupThreads(annotation.eventExecutorGroupThreads()), Integer.class, "eventExecutorGroupThreads");

        String sslKeyPassword = resolveAnnotationValue(configureConfig.getSslKeyPassword(annotation.sslKeyPassword()), String.class, "sslKeyPassword");
        String sslKeyStore = resolveAnnotationValue(configureConfig.getSslKeyStore(annotation.sslKeyStore()), String.class, "sslKeyStore");
        String sslKeyStorePassword = resolveAnnotationValue(configureConfig.getSslKeyStorePassword(annotation.sslKeyStorePassword()), String.class, "sslKeyStorePassword");
        String sslKeyStoreType = resolveAnnotationValue(configureConfig.getSslKeyStoreType(annotation.sslKeyStoreType()), String.class, "sslKeyStoreType");
        String sslTrustStore = resolveAnnotationValue(configureConfig.getSslTrustStore(annotation.sslTrustStore()), String.class, "sslTrustStore");
        String sslTrustStorePassword = resolveAnnotationValue(configureConfig.getSslTrustStorePassword(annotation.sslTrustStorePassword()), String.class, "sslTrustStorePassword");
        String sslTrustStoreType = resolveAnnotationValue(configureConfig.getSslTrustStoreType(annotation.sslTrustStoreType()), String.class, "sslTrustStoreType");

        String[] corsOrigins = configureConfig.getCorsOrigins(annotation.corsOrigins());
        if (corsOrigins.length != 0) {
            for (int i = 0; i < corsOrigins.length; i++) {
                corsOrigins[i] = resolveAnnotationValue(corsOrigins[i], String.class, "corsOrigins");
            }
        }
        Boolean corsAllowCredentials = resolveAnnotationValue(configureConfig.getCorsAllowCredentials(annotation.corsAllowCredentials()), Boolean.class, "corsAllowCredentials");

        ServerEndpointConfig serverEndpointConfig = new ServerEndpointConfig(host, port, bossLoopGroupThreads, workerLoopGroupThreads
                , useCompressionHandler, optionConnectTimeoutMillis, optionSoBacklog, childOptionWriteSpinCount, childOptionWriteBufferHighWaterMark
                , childOptionWriteBufferLowWaterMark, childOptionSoRcvbuf, childOptionSoSndbuf, childOptionTcpNodelay, childOptionSoKeepalive
                , childOptionSoLinger, childOptionAllowHalfClosure, readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds
                , maxFramePayloadLength, useEventExecutorGroup, eventExecutorGroupThreads
                , sslKeyPassword, sslKeyStore, sslKeyStorePassword, sslKeyStoreType
                , sslTrustStore, sslTrustStorePassword, sslTrustStoreType
                , corsOrigins, corsAllowCredentials);

        return serverEndpointConfig;
    }

    private <T> T resolveAnnotationValue(Object value, Class<T> requiredType, String paramName) {
        if (value == null) {
            return null;
        }
        TypeConverter typeConverter = beanFactory.getTypeConverter();

        if (value instanceof String) {
            String strVal = beanFactory.resolveEmbeddedValue((String) value);
            BeanExpressionResolver beanExpressionResolver = beanFactory.getBeanExpressionResolver();
            if (beanExpressionResolver != null) {
                value = beanExpressionResolver.evaluate(strVal, new BeanExpressionContext(beanFactory, null));
            } else {
                value = strVal;
            }
        }
        try {
            return typeConverter.convertIfNecessary(value, requiredType);
        } catch (TypeMismatchException e) {
            throw new IllegalArgumentException("Failed to convert value of parameter '" + paramName + "' to required type '" + requiredType.getName() + "'");
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
