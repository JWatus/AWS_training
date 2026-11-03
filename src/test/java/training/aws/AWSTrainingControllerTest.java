package training.aws;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import training.aws.api.AWSTrainingController;
import training.aws.entity.User;
import training.aws.service.RDSService;
import training.aws.service.S3Service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AWSTrainingController.class)
class AWSTrainingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private S3Service s3Service;

  @MockitoBean
  private RDSService rdsService;

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

  @Test
  void createUserReturnsCreatedUser() throws Exception {
    User user = new User("Jakub", "AWS trainee");
    user.setId(1L);
    when(rdsService.createUser("Jakub", "AWS trainee")).thenReturn(user);

    mockMvc.perform(post("/training/aws/rds/user")
            .param("userName", "Jakub")
            .param("userDescription", "AWS trainee"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.userName").value("Jakub"))
        .andExpect(jsonPath("$.userDescription").value("AWS trainee"));
  }

  @Test
  void getUserReturnsUser() throws Exception {
    User user = new User("Jakub", "AWS trainee");
    user.setId(1L);
    when(rdsService.getUser("Jakub")).thenReturn(user);

    mockMvc.perform(get("/training/aws/rds/user").param("userName", "Jakub"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value("Jakub"));
  }

  @Test
  void updateUserReturnsUpdatedUser() throws Exception {
    User user = new User("Jakub", "Updated description");
    user.setId(1L);
    when(rdsService.updateUser("Jakub", "Updated description")).thenReturn(user);

    mockMvc.perform(put("/training/aws/rds/user")
            .param("userName", "Jakub")
            .param("userDescription", "Updated description"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userDescription").value("Updated description"));
  }

  @Test
  void deleteUserReturnsConfirmation() throws Exception {
    doNothing().when(rdsService).deleteUser("Jakub");

    mockMvc.perform(delete("/training/aws/rds/user").param("userName", "Jakub"))
        .andExpect(status().isOk())
        .andExpect(content().string("User Jakub deleted"));
  }
}
