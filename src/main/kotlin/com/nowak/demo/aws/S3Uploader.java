package com.nowak.demo.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class S3Uploader {

    private static final Properties properties = new Properties();
    private static final String CONTENT_TYPE = "application/pdf";
    private final InputStream inputStream;

    private static BasicAWSCredentials basicAWSCredentials;
    private static AmazonS3 amazonS3;
    private static ObjectMetadata objectMetadata;
    private static String ACCESS_KEY;
    private static String SECRET_KEY;
    private static String BUCKET_NAME;
    private static String REGION;

    public S3Uploader() throws IOException {
        inputStream = getClass().getClassLoader().getResourceAsStream("app.properties");

        properties.load(inputStream);

        ACCESS_KEY = properties.getProperty("cloud.aws.credentials.accessKey");
        SECRET_KEY = properties.getProperty("cloud.aws.credentials.secretKey");
        BUCKET_NAME = properties.getProperty("aws.bucket.name");
        REGION = properties.getProperty("aws.region.name");
    }

    public void initAWSClient() {
        basicAWSCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        amazonS3 = AmazonS3Client.builder()
                .withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();

        objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(CONTENT_TYPE);
    }

    public String upload(String fileName, String path) {
        URL url = null;
        initAWSClient();
        try {
            amazonS3.putObject(BUCKET_NAME, fileName, new File(path));
            url = amazonS3.getUrl(BUCKET_NAME, fileName);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"An Error occured while uploading invoice.");
        }
        return url.toString();
    }
    public String getLink(String invoiceNo){
        URL url = amazonS3.getUrl(BUCKET_NAME, invoiceNo);
        return url.toString();
    }
}
