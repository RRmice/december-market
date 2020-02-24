package com.geekbrains.decembermarket;

import com.geekbrains.decembermarket.beans.Cart;
import com.geekbrains.decembermarket.entites.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarTest {

	@Autowired
	private Cart cart;

	@Test
	public void cartAddTest() {
		for (int i = 0; i < 5; i++) {
			Product product = new Product();
			product.setId(new Long(i + 1));
			product.setPrice(new BigDecimal(100 + i * 10));
			product.setTitle("Product #" + i);
			cart.add(product);
		}
		Assert.assertEquals(5, cart.getItems().size());
	}

}
