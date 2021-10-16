package ru.itis.cloudphoto;

import com.beust.jcommander.JCommander;
import ru.itis.cloudphoto.commads.ListArgs;
import ru.itis.cloudphoto.commads.LoadArgs;
import ru.itis.cloudphoto.service.ObjectStorageService;

import java.util.Arrays;
import java.util.List;

public class Main {

    private static final String UPLOAD_PARAM = "upload";
    private static final String DOWNLOAD_PARAM = "download";
    private static final String LIST_PARAM = "list";

    private static LoadArgs buildLoadArgs(String[] args) {
        LoadArgs loadArgs = new LoadArgs();
        JCommander.newBuilder()
                .addObject(loadArgs)
                .build()
                .parse(args);
        return loadArgs;
    }

    public static void main(String[] args) {
        List<String> argList = Arrays.asList(args);
        boolean isUpload = argList.contains(UPLOAD_PARAM);
        boolean isDownload = argList.contains(DOWNLOAD_PARAM);
        boolean isList = argList.contains(LIST_PARAM);

        ObjectStorageService objectStorageService = new ObjectStorageService();

        if (isUpload) {
            LoadArgs loadArgs = buildLoadArgs(args);
            objectStorageService.upload(loadArgs);
        }

        if (isDownload) {
            LoadArgs loadArgs = buildLoadArgs(args);
            objectStorageService.download(loadArgs);
        }

        if (isList) {
            ListArgs listArgs = new ListArgs();
            JCommander.newBuilder()
                    .addObject(listArgs)
                    .build()
                    .parse(args);
            if (listArgs.getAlbum() == null) {
                objectStorageService.getAlbums().forEach(System.out::println);
            } else {
                objectStorageService.getPictures(listArgs).forEach(System.out::println);
            }
        }


    }
}
