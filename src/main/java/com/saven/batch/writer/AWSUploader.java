package com.saven.batch.writer;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by nrege on 1/28/2017.
 */
public class AWSUploader implements StepExecutionListener {

    String folderName;

    String accessKey;

    String secretKey;

    String bucketName;

    String key;

    boolean upload;

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        upload();
        return stepExecution.getExitStatus();
    }

    private void upload() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy"); //-hh:mma

        String dateTime = LocalDateTime.now().format(formatter);
        System.out.println(dateTime);
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null && upload) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    File file = listOfFiles[i];
                    if (file.exists() && !file.isDirectory()) {
                        try {
                            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
                            AmazonS3 s3client = new AmazonS3Client(credentials);
                            s3client.putObject(new PutObjectRequest(bucketName, key+dateTime+".png", file));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }
}
