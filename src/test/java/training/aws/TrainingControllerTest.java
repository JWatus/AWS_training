package training.aws;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
class TrainingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testStringReturnsAwsTraining() throws Exception {
    mockMvc.perform(get("/training/aws/testString"))
        .andExpect(status().isOk())
        .andExpect(content().string("AWS training"));
  }
}
