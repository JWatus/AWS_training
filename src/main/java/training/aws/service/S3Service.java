package training.aws.service;

import java.time.Duration;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

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

  public String generatePresignedUrl(String name) {
    String fileName = "uploads/hello_" + name + ".txt";

    try (S3Presigner presigner = S3Presigner.create()) {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(BUCKET_NAME)
          .key(fileName)
          .build();

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(10))
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

      return presignedRequest.url().toString();
    }
  }
}
