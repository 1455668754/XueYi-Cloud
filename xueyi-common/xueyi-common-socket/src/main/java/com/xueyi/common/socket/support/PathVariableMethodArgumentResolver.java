package com.xueyi.common.socket.support;

import com.xueyi.common.socket.annotation.PathVariable;
import io.netty.channel.Channel;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.MethodParameter;

import java.util.Map;

import static com.xueyi.common.socket.pojo.PojoEndpointServer.URI_TEMPLATE;

public class PathVariableMethodArgumentResolver implements MethodArgumentResolver {

    private AbstractBeanFactory beanFactory;

    public PathVariableMethodArgumentResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        PathVariable ann = parameter.getParameterAnnotation(PathVariable.class);
        String name = ann.name();
        if (name.isEmpty()) {
            name = parameter.getParameterName();
            if (name == null) {
                throw new IllegalArgumentException(
                        "Name for argument type [" + parameter.getNestedParameterType().getName() +
                                "] not available, and parameter name information not found in class file either.");
            }
        }
        Map<String, String> uriTemplateVars = channel.attr(URI_TEMPLATE).get();
        Object arg = (uriTemplateVars != null ? uriTemplateVars.get(name) : null);
        TypeConverter typeConverter = beanFactory.getTypeConverter();
        return typeConverter.convertIfNecessary(arg, parameter.getParameterType());
    }
}
