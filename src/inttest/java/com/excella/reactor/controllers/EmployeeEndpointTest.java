package com.excella.reactor.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.excella.reactor.domain.*;
import com.excella.reactor.util.TestSecUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import java.time.LocalDate;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class EmployeeEndpointTest extends AbstractTestNGSpringContextTests {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;
  @Autowired private TestSecUtils testSecUtils;
  @Autowired private EmployeeController employeeController;

  private static final String ENDPOINT = "/employee";
  private Employee employee;
  private String authToken;

  @BeforeClass
  public void beforeClass() {
    this.authToken = testSecUtils.getAuth(mockMvc);
  }

  @BeforeMethod
  public void beforeTest() {
    employee = new Employee();
    var bio = new Bio();
    var contact = new Contact();
    var employeeSkill = new EmployeeSkill();
    var skill = new Skill();
    var address = new Address();
    var skillCategory = new SkillCategory();

    skillCategory.setId(1L);
    skillCategory.setName("Agile");
    skill.setId(1L);
    skill.setName("Scrum Master");
    skill.setCategory(skillCategory);
    employee.setBio(bio);
    employee.setContact(contact);
    employee.setSkills(Collections.singletonList(employeeSkill));

    bio.setEthnicity(Ethnicity.CAUCASIAN);
    bio.setFirstName("John");
    bio.setLastName("Doe");
    bio.setGender(Gender.MALE);
    bio.setUsCitizen(Boolean.TRUE);
    bio.setBirthDate(LocalDate.now().minusYears(18));

    contact.setAddress(address);
    contact.setEmail("john.doe@test.com");
    contact.setPhoneNumber("(571)555-5555");

    address.setLine1("1 Fake St");
    address.setCity("Portsmouth");
    address.setStateCode("VA");
    address.setZipCode("23523");

    employeeSkill.setSkill(skill);
    employeeSkill.setProficiency(SkillProficiency.HIGH);
    employeeSkill.setPrimary(Boolean.TRUE);

    skill.setId(1L);
  }

  @Test
  public void contextLoads() {
    assert mockMvc != null;
    assert mapper != null;
    assert employeeController != null;
  }

  @Test(description = "Should reject unauthorized user.")
  public void authError() throws Exception {
    mockMvc
        .perform(
            post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(employee)))
        .andExpect(status().isUnauthorized());
  }

  @Test(description = "Should post a valid employee.")
  public void postSuccess() throws Exception {
    mockMvc
        .perform(
            post(ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(employee)))
        .andExpect(status().is2xxSuccessful());
  }

  @Test(description = "Should successfully get employee")
  public void getSuccess() throws Exception {
    mockMvc
        .perform(
            get(ENDPOINT).header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authToken)))
        .andExpect(status().is2xxSuccessful());
  }
}
