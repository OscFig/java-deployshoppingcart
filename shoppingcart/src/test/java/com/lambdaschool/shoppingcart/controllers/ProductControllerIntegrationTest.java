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
public class ProductControllerIntegrationTest
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
    public void listAllProducts() throws Exception
    {
        this.mockMvc.perform(get("/products/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("PENCIL")));
    }

    @Test
    public void getProductById() throws Exception
    {
        this.mockMvc.perform(get("/products/product/{productid}",
            7))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("PENCIL")));
    }

    @Test
    public void getUserByIdNotFound() throws
                                      Exception
    {
        this.mockMvc.perform(get("/products/product/{productid}",
            100))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(content().string(containsString("ResourceNotFoundException")));
    }

    @Test
    public void addProduct() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/products/product")
        .content("{\"name\": \"PUPPY\", \"price\": 300.00, \"description\" : \"GINGER\"}")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.header()
            .exists("location"));
    }

    @Test
    public void updateProductById() throws Exception
    {
            mockMvc.perform(MockMvcRequestBuilders.put("/products/product/{productid}",
                8)
                .content("{\"name\": \"ESPRESSO\", \"price\": 5.75, \"description\" : \"Coffee Squared\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProductById() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/product/{id}",
            6))
            .andDo(print())
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteProductByIdNotFound() throws
                                         Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/product/{id}",
            100))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }
}