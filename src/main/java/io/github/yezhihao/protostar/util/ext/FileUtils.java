package io.github.yezhihao.protostar.util.ext;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author baoqingyun
 */
public class FileUtils {
    public static String getFileExtension(@NotNull String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getNameWithoutExtension(@NotNull String file) {
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
}
