package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.ShoppingCartTestApplication;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = ShoppingCartTestApplication.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "barnbarn")
public class RolesControllerIntegrationTest
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws
                        Exception
    {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listRoles() throws Exception
    {
        this.mockMvc.perform(get("/roles/roles"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("ADMIN")));
    }

    @Test
    public void getRoleById() throws Exception
    {
        this.mockMvc.perform(get("/roles/role/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("ADMIN")));
    }

    @Test
    public void getRoleByIdNotFound() throws
                                      Exception
    {
        this.mockMvc.perform(get("/roles/role/{roleid}",
            100))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(content().string(containsString("ResourceNotFoundException")));
    }

    @Test
    public void getRoleByName() throws Exception
    {
        this.mockMvc.perform(get("/roles/role/name/{roleName}",
            "admin"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("ADMIN")));
    }

    public void getRoleByNameNotFound() throws
                                        Exception
    {
        this.mockMvc.perform(get("/roles/role/name/{roleName}",
            "rabbit"))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(content().string(containsString("ResourceNotFoundException")));
    }

    @Test
    public void addNewRole() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/roles/role")
            .content("{\"name\": \"ANEWROLE\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.header()
                .exists("location"));
    }

    @Test
    public void putUpdateRole() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.put("/roles/role/{roleid}",
            2)
            .content("{\"name\": \"ANOTHERROLE\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }
}