package org.fauman.appleworm.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;

public class FileHelper {

    public static ImmutableList<String> getChildren(String path) throws URISyntaxException, IOException {
        URL url = FileHelper.class.getResource(path);
        if(url == null) return ImmutableList.of();
        FileSystem fileSystem;
        try{
            fileSystem = FileSystems.newFileSystem(url.toURI(), Collections.emptyMap());
        } catch (FileSystemAlreadyExistsException e){
            fileSystem = FileSystems.getFileSystem(url.toURI());
        } catch (IllegalArgumentException e) {
            File[] files = new File(url.getPath()).listFiles();
            if(files == null) return ImmutableList.of();
            return Arrays.stream(files)
                    .map(File::getName)
                    .map(s -> s.substring(0, s.indexOf(".")))
                    .collect(ImmutableList.toImmutableList());
        }
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fileSystem.getPath(path));
        return Streams.stream(directoryStream.iterator())
                .map(p -> p.getFileName().toString())
                .map(s -> s.substring(0, s.indexOf(".")))
                .collect(ImmutableList.toImmutableList());
    }
}
