package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import kitchenpos.application.ProductService;
import kitchenpos.config.TestConfig;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductRestController.class)
@Import(TestConfig.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("상품이 생성에 실패하면 400 Bad Request를 응답한다.")
    @Test
    void create_product_if_failed_then_responds_400_bad_request() throws Exception {
        // given
        var content = "{\"name\": \"\", \"price\": 0}";
        given(productService.create(any())).willThrow(new IllegalArgumentException());

        // when
        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                // then
                .andExpect(status().isBadRequest());
    }

    @DisplayName("상품이 생성에 성공하면 201 Created를 응답한다.")
    @Test
    void create_product_if_succeeds_then_responds_201_created() throws Exception {
        // given
        var content = "{\"name\": \"상품\", \"price\": 1000}";
        var product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("상품");
        given(productService.create(any())).willReturn(product);

        // when
        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                // then
                .andExpect(status().isCreated());
    }
}
