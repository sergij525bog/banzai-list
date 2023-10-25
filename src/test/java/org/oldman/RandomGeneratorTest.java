package org.oldman;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.oldman.entities.Product;

public class RandomGeneratorTest {
    @Test
    public void itShouldCreateTestEntity() {
        Product product = Instancio.create(Product.class);
        System.out.println(product);
    }
}
