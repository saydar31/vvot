package ru.itis.cloudphoto.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import ru.itis.cloudphoto.Main;
import ru.itis.cloudphoto.commads.ListArgs;
import ru.itis.cloudphoto.commads.LoadArgs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObjectStorageService {

    private final AmazonS3 s3;
    private static final String BUCKET_NAME = "d40.itiscl.ru";

    public ObjectStorageService() {
        try {
            s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new PropertiesCredentials(Main.class.getResourceAsStream("/aws.properties"))))
                    .withEndpointConfiguration(
                            new AmazonS3ClientBuilder.EndpointConfiguration(
                                    "storage.yandexcloud.net", "ru-central1"
                            )
                    )
                    .build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final String ALBUM_PREFIX = "album_";
    private static final String ALBUM_NAME_SEPARATOR = "_";


    public void upload(LoadArgs loadArgs) {
        for (File file : Objects.requireNonNull(loadArgs.getPath().listFiles())) {
            if (file.getName().endsWith(".jpeg") || file.getName().endsWith(".jpg")) {
                s3.putObject(BUCKET_NAME, ALBUM_PREFIX + loadArgs.getAlbum() + ALBUM_NAME_SEPARATOR + file.getName(), file);
            }
        }

    }

    public void download(LoadArgs loadArgs) {
        String albumPrefix = ALBUM_PREFIX + loadArgs.getAlbum() + ALBUM_NAME_SEPARATOR;
        List<S3ObjectSummary> objectSummaries = s3.listObjects(BUCKET_NAME, albumPrefix)
                .getObjectSummaries();
        for (S3ObjectSummary objectSummary : objectSummaries) {
            String key = objectSummary.getKey();
            S3ObjectInputStream objectContent = s3.getObject(BUCKET_NAME, key).getObjectContent();
            String fileName = key.substring(key.lastIndexOf(albumPrefix) + albumPrefix.length());
            try {
                Files.copy(objectContent,
                        new File(loadArgs.getPath(), fileName).toPath());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }


    }

    public List<String> getAlbums() {
        List<S3ObjectSummary> objectSummaries = s3.listObjects(BUCKET_NAME)
                .getObjectSummaries();
        return objectSummaries.stream()
                .map(S3ObjectSummary::getKey)
                .filter(key -> key.startsWith(ALBUM_PREFIX))
                .map(key -> key.substring(key.indexOf(ALBUM_PREFIX) + ALBUM_PREFIX.length()))
                .filter(key -> key.contains(ALBUM_NAME_SEPARATOR))
                .map(key -> key.substring(0, key.indexOf(ALBUM_NAME_SEPARATOR)))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getPictures(ListArgs listArgs) {
        String albumPrefix = ALBUM_PREFIX + listArgs.getAlbum() + ALBUM_NAME_SEPARATOR;
        List<S3ObjectSummary> objectSummaries = s3.listObjects(BUCKET_NAME, albumPrefix)
                .getObjectSummaries();
        return objectSummaries.stream()
                .map(S3ObjectSummary::getKey)
                .filter(key -> key.startsWith(albumPrefix))
                .map(key -> key.substring(albumPrefix.length()))
                .collect(Collectors.toList());
    }
}
