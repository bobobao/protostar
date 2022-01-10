package io.github.yezhihao.protostar;

import groovy.lang.GroovyClassLoader;
import io.github.yezhihao.protostar.annotation.Message;
import io.github.yezhihao.protostar.ext.groovy.GroovyClzLoaderWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author baoqingyun
 */
public class GroovySchemaManager extends SchemaManager {

    private final GroovyClzLoaderWrapper clzLoaderWrapper = new GroovyClzLoaderWrapper(new GroovyClassLoader(), new GroovyClzLoaderWrapper.Config());

    public GroovySchemaManager(Set<String> paths) throws IOException {
        this(128, paths);
    }

    public GroovySchemaManager(int initialCapacity, Set<String> paths) throws IOException {
        super(initialCapacity);
        load(paths);
    }

    private void load(Set<String> paths) throws IOException {
        List<Class<?>> types = clzLoaderWrapper.getClassList(paths);
        for (Class<?> type : types) {
            Message message = type.getAnnotation(Message.class);
            if (message != null) {
                int[] values = message.value();
                for (int typeId : values) {
                    loadRuntimeSchema(typeId, type);
                }
            }
        }
    }

    public GroovyClassLoader getGroovyClassLoader() {
        return clzLoaderWrapper.delegate;
    }
}
