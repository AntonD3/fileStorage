package com.example.fileStorage.util;

import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FileUtils {
    public boolean isFilenameValid(String file) {
        if(file == null) return false;
        if(file.equals("")) return false;
        java.io.File f = new java.io.File(file);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isSizeValid(Integer size)
    {
        if(size == null) return false;
        return size >= 0;
    }

    public String getFileContentType(String file)
    {
        MediaType mediaType = MediaTypeFactory.getMediaType(file).orElse(null);

        if(mediaType == null)
            return null;

        return mediaType.getType();

    }

}
