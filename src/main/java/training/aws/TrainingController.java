package training.aws;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainingController {

  @GetMapping("/training/aws/testString")
  public String testString() {
    return "AWS training";
  }
}
