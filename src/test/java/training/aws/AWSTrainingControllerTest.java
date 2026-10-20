package training.aws;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import training.aws.api.AWSTrainingController;
import training.aws.service.S3Service;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AWSTrainingController.class)
class AWSTrainingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private S3Service s3Service;

  @Test
  void getHelloFromEC2ReturnsString() throws Exception {
    mockMvc.perform(get("/training/aws/ec2/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello from EC2"));
  }

  @Test
  void uploadToS3ReturnsServiceResult() throws Exception {
    when(s3Service.uploadFile("Jakub")).thenReturn("File hello_Jakub.txt uploaded to S3");

    mockMvc.perform(post("/training/aws/s3").param("name", "Jakub"))
        .andExpect(status().isOk())
        .andExpect(content().string("File hello_Jakub.txt uploaded to S3"));
  }

  @Test
  void getPresignedUrlReturnsServiceResult() throws Exception {
    when(s3Service.generatePresignedUrl("Jakub")).thenReturn("https://example.com/presigned");

    mockMvc.perform(get("/training/aws/s3/presigned-url").param("name", "Jakub"))
        .andExpect(status().isOk())
        .andExpect(content().string("https://example.com/presigned"));
  }
}
