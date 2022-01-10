package io.github.yezhihao.protostar.ext.groovy;

import groovy.lang.GroovyClassLoader;
import io.github.yezhihao.protostar.util.ext.FileUtils;
import io.github.yezhihao.protostar.util.lambda.ThrowingFunction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author baoqingyun
 */
public class GroovyClzLoaderWrapper {
    private static final Logger log = LoggerFactory.getLogger(GroovyClzLoaderWrapper.class);

    public final GroovyClassLoader delegate;
    private final Config config;

    public GroovyClzLoaderWrapper(GroovyClassLoader delegate, Config config) {
        this.delegate = delegate;
        this.config = config;
    }

    public @NotNull List<Class<?>> getClassList(Set<String> paths) throws IOException {
        List<Class<?>> clzList = new LinkedList<>();
        for (String pathStr : paths) {
            Path path = Paths.get(pathStr);
            if (path.getNameCount() == 0) {
                log.warn("Groovy loading path: " + pathStr + " is a root component.");
                continue;
            }
            if (!Files.isDirectory(path)) {
                log.warn("Groovy script loading path: " + pathStr + " is not a directory!");
                continue;
            }
            try (Stream<Path> walk = Files.walk(path, 1)) {
                walk.filter(it -> FileUtils.getFileExtension(it.toString()).equals("groovy"))
                        .map(ThrowingFunction.throwingFunctionWrapper(it -> delegate.parseClass(it.toFile())))
                        .forEach(clzList::add);
            }
        }
        List<String> duplicateElements = getDuplicateElements(clzList.stream().map(Class::getName));
        if (!duplicateElements.isEmpty()) {
            log.error("Duplicate class. Class name: " + duplicateElements + ".");
            throw new IllegalArgumentException("Duplicate class. Class name: " + duplicateElements + ".");
        }
        return clzList;
    }

    private <E> List<E> getDuplicateElements(Stream<E> stream) {
        return stream
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static class Config {

    }
}
