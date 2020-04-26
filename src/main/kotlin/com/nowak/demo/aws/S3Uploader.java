package com.nowak.demo.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class S3Uploader {

    private static final Properties properties = new Properties();
    private static InputStream inputStream;

    static {
        try {
            inputStream = new FileInputStream("app.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static BasicAWSCredentials basicAWSCredentials;
    private static AmazonS3 amazonS3;
    private static ObjectMetadata objectMetadata;
    private static String BUCKET_NAME;

    public S3Uploader() throws FileNotFoundException {
    }

    public static void initAWSClient() {
        basicAWSCredentials = new BasicAWSCredentials(
                properties.getProperty("cloud.aws.credentials.accessKey"), properties.getProperty("cloud.aws.credentials.secretKey"));
        BUCKET_NAME = properties.getProperty("aws.bucket.name");
        amazonS3 = AmazonS3Client.builder()
                .withRegion("eu-central-1")
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();

        objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/pdf");
    }

    public static String upload(String fileName) {

        initAWSClient();

        //TODO

        return "false";
    }
}
