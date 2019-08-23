package com.excella.reactor.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.excella.reactor.domain.Skill;
import com.excella.reactor.domain.SkillCategory;
import com.excella.reactor.util.TestSecUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class SkillEndpointTest extends AbstractTestNGSpringContextTests {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;
  @Autowired private TestSecUtils testSecUtils;
  @Autowired private SkillController skillController;

  private static final String ENDPOINT = "/skills";
  private Skill skill = new Skill();
  private String authToken;

  @BeforeClass
  public void beforeClass() {
    this.authToken = testSecUtils.getAuth(mockMvc);
  }

  @BeforeTest
  public void beforeTest() {
    var skillCategory = new SkillCategory();
    skillCategory.setId(1L);
    skillCategory.setName("Agile");
    skill.setName("Scrum Master");
    skill.setCategory(skillCategory);
  }

  @Test
  public void contextLoads() {
    assert mockMvc != null;
    assert mapper != null;
    assert skillController != null;
  }

  @Test(description = "Should reject unauthorized user.")
  public void authError() throws Exception {
    mockMvc
        .perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(skill)))
        .andExpect(status().isUnauthorized());
  }

  // TODO figure out why this isn't working (category is deserializing to null)
  @Ignore
  @Test(description = "Should post a valid skills.")
  public void postSuccess() throws Exception {
    mockMvc
        .perform(
            post(ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(skill)))
        .andExpect(status().is2xxSuccessful());
  }

  @Test(priority = 1, description = "Should successfully get all skills with no pagination.")
  public void getSuccess() throws Exception {
    mockMvc
        .perform(
            get(ENDPOINT).header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authToken)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(
            jsonPath("$._embedded.skills", hasSize(24))); // there are 24 skills in the mock data
  }

  @Test(priority = 1, description = "Ensure pagination is working")
  public void getPagination() throws Exception {
    var pageSizes = new int[] {1, 2, 3, 5, 8, 13, 20, 21};
    for (int pageSize : pageSizes) {
      mockMvc
          .perform(
              get(ENDPOINT)
                  .param("size", Integer.toString(pageSize))
                  .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authToken)))
          .andExpect(status().is2xxSuccessful())
          .andExpect(jsonPath("$._embedded.skills", hasSize(pageSize)));
    }
  }
}
