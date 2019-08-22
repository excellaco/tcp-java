package com.excella.reactor.controllers;

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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class SkillEndpointTest extends AbstractTestNGSpringContextTests {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;
  @Autowired private TestSecUtils testSecUtils;
  @Autowired private SkillController skillController;

  private static final String ENDPOINT = "/skills";
  private List<Skill> skills = new ArrayList<>();
  private String authToken;

  @BeforeSuite
  public void beforeSuite() {
    var skillCategory = new SkillCategory();
    var skillNames = new ArrayList<String>();
    for (int i = 1; i <= 25; i++) {
      skillNames.add(String.format("%s %s", "Development", i));
    }
    skillCategory.setId(1L);
    skillCategory.setName("Dev");
    for (String skillName: skillNames) {
      var skill = new Skill();
      skill.setName(skillName);
      skill.setCategory(skillCategory);
      skills.add(skill);
    }
  }

  @BeforeClass
  public void beforeClass() {
    this.authToken = testSecUtils.getAuth(mockMvc);
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
                .content(mapper.writeValueAsString(skills.get(0))))
        .andExpect(status().isUnauthorized());
  }

  @Test(priority = 1, description = "Should post a valid skills.")
  public void postSuccess() throws Exception {
    for (Skill skill: skills) {
      mockMvc
          .perform(
              post(ENDPOINT)
                  .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authToken))
                  .contentType(MediaType.APPLICATION_JSON_UTF8)
                  .content(mapper.writeValueAsString(skill)))
          .andExpect(status().is2xxSuccessful());
    }
  }

  @Test(priority = 2, description = "Should successfully get all skills")
  public void getSuccess() throws Exception {
    mockMvc
        .perform(
            get(ENDPOINT).header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authToken)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize(skills.size())));
  }

}
