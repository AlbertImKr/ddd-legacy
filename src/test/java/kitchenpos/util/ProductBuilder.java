package kitchenpos.util;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.Product;

public class ProductBuilder {

    private UUID id;
    private BigDecimal price;

    private ProductBuilder() {
    }

    public static ProductBuilder id(UUID id) {
        ProductBuilder productBuilder = new ProductBuilder();
        productBuilder.id = id;
        return productBuilder;
    }

    public ProductBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build() {
        var targetProduct = new Product();
        targetProduct.setId(id);
        targetProduct.setPrice(price);
        return targetProduct;
    }
}
