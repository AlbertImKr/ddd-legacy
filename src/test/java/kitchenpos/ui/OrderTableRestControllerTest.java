package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.UUID;
import kitchenpos.application.OrderTableService;
import kitchenpos.config.TestConfig;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@WebMvcTest(OrderTableRestController.class)
@Import(TestConfig.class)
class OrderTableRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderTableService orderTableService;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("주문 테이블 생성")
    @Nested
    class CreateOrderTable {

        @DisplayName("주문 테이블 생성 실패하면 400 Bad Request를 응답한다.")
        @Test
        void if_failed_then_responds_400_bad_request() throws Exception {
            // given
            var name = "";
            var body = new HashMap<String, Object>() {{
                put("name", name);
            }};
            var content = objectMapper.writeValueAsString(body);
            given(orderTableService.create(any())).willThrow(new IllegalArgumentException());

            // when
            mockMvc.perform(post("/api/order-tables")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content))
                    // then
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("주문 테이블 생성 성공하면 201 Created를 응답한다.")
        @Test
        void if_succeed_then_responds_201_created() throws Exception {
            // given
            var name = "테이블";
            var body = new HashMap<String, Object>() {{
                put("name", name);
            }};
            var content = objectMapper.writeValueAsString(body);

            var menuTableId = UUID.randomUUID();
            var menuTable = new OrderTable();
            menuTable.setName(name);
            menuTable.setId(menuTableId);

            given(orderTableService.create(any())).willReturn(menuTable);

            // when
            var result = mockMvc.perform(post("/api/order-tables")
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(content))
                    // then
                    .andExpect(status().isCreated())
                    .andReturn();

            var uri = result.getResponse().getHeader("Location");
            assertThat(uri).isEqualTo("/api/order-tables/" + menuTableId);
        }
    }
}
