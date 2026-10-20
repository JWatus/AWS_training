package training.aws.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import training.aws.service.S3Service;

@RestController
public class AWSTrainingController {

  private final S3Service s3Service;

  public AWSTrainingController(S3Service s3Service) {
    this.s3Service = s3Service;
  }

  @GetMapping("/training/aws/ec2/hello")
  public String getHelloFromEC2() {
    return "Hello from EC2";
  }

  @PostMapping("/training/aws/s3")
  public String uploadToS3(@RequestParam String name) {
    return s3Service.uploadFile(name);
  }
}
