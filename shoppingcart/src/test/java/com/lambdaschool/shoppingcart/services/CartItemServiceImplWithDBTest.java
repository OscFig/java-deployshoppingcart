package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingCartTestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartTestApplication.class)
public class CartItemServiceImplWithDBTest
{
    @Autowired
    private CartItemService cartItemService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void addToCart()
    {
        assertEquals(5,
            cartItemService.addToCart(3,
                6,
                "Hello")
                .getQuantity());
    }

    @Test
    public void removeFromCart()
    {
        assertEquals(2,
            cartItemService.removeFromCart(3,
                7,
                "Bye")
                .getQuantity());
    }

    @Test
    public void emptyFromCart()
    {
        assertNull(cartItemService.removeFromCart(4,
            8,
            "Bye"));
    }
}