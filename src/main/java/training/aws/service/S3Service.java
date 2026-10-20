package training.aws.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

  private final S3Client s3Client;
  private static final String BUCKET_NAME = "my-bucket-946298553912-eu-central-1-an";

  public S3Service() {
    this.s3Client = S3Client.builder().build();
  }

  public String uploadFile(String name) {
    String fileName = "hello_" + name + ".txt";
    String content = "Hello " + name + "!";

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(BUCKET_NAME)
        .key("uploads/" + fileName)
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromString(content));

    return "File " + fileName + " uploaded to S3";
  }
}
