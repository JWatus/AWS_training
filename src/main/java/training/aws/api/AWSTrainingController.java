package training.aws.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import training.aws.entity.User;
import training.aws.service.RDSService;
import training.aws.service.S3Service;

@RestController
@RequestMapping("/training/aws")
public class AWSTrainingController {

  private final S3Service s3Service;
  private final RDSService rdsService;

  public AWSTrainingController(S3Service s3Service, RDSService rdsService) {
    this.s3Service = s3Service;
    this.rdsService = rdsService;
  }

  @GetMapping("/ec2/hello")
  public String getHelloFromEC2() {
    return "Hello from EC2";
  }

  @PostMapping("/s3")
  public String uploadToS3(@RequestParam String name) {
    return s3Service.uploadFile(name);
  }

  @GetMapping("/s3/presigned-url")
  public String getPresignedUrl(@RequestParam String name) {
    return s3Service.generatePresignedUrl(name);
  }

  @PostMapping("/rds/user")
  public User createUser(
      @RequestParam String userName,
      @RequestParam(required = false) String userDescription) {
    return rdsService.createUser(userName, userDescription);
  }

  @GetMapping("/rds/user")
  public User getUser(@RequestParam String userName) {
    return rdsService.getUser(userName);
  }

  @PutMapping("/rds/user")
  public User updateUser(
      @RequestParam String userName,
      @RequestParam(required = false) String userDescription) {
    return rdsService.updateUser(userName, userDescription);
  }

  @DeleteMapping("/rds/user")
  public String deleteUser(@RequestParam String userName) {
    rdsService.deleteUser(userName);
    return "User " + userName + " deleted";
  }
}
