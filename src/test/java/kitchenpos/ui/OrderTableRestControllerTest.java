package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.NoSuchElementException;
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

    @DisplayName("주문 테이블 사용")
    @Nested
    class SitOrderTable {

        @DisplayName("주문 테이블이 존재하지 않는 경우 예외를 던진다.")
        @Test
        void if_order_table_does_not_exist_then_throw_exception() throws Exception {
            // given
            var orderTableId = UUID.randomUUID();

            given(orderTableService.sit(orderTableId)).willThrow(new NoSuchElementException());

            // when then
            mockMvc.perform(put("/api/order-tables/{orderTableId}/sit", orderTableId))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("주문 테이블을 사용하면 주문 테이블을 반환한다.")
        @Test
        void if_succeed_then_return_order_table() throws Exception {
            // given
            var orderTableId = UUID.randomUUID();
            var orderTable = new OrderTable();
            orderTable.setId(orderTableId);

            given(orderTableService.sit(orderTableId)).willReturn(orderTable);

            // when
            var result = mockMvc.perform(put("/api/order-tables/{orderTableId}/sit", orderTableId))
                    // then
                    .andExpect(status().isOk())
                    .andReturn();

            var response = objectMapper.readValue(result.getResponse().getContentAsString(), OrderTable.class);
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(orderTableId);
        }
    }

    @DisplayName("주문 테이블 비우기")
    @Nested
    class ClearOrderTable {

        @DisplayName("주문 테이블이 존재하지 않는 경우 예외를 던진다.")
        @Test
        void if_order_table_does_not_exist_then_throw_exception() throws Exception {
            // given
            var orderTableId = UUID.randomUUID();

            given(orderTableService.clear(orderTableId)).willThrow(new NoSuchElementException());

            // when then
            mockMvc.perform(put("/api/order-tables/{orderTableId}/clear", orderTableId))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("주문 테이블을 비우면 주문 테이블을 반환한다.")
        @Test
        void if_succeed_then_return_order_table() throws Exception {
            // given
            var orderTableId = UUID.randomUUID();
            var orderTable = new OrderTable();
            orderTable.setId(orderTableId);

            given(orderTableService.clear(orderTableId)).willReturn(orderTable);

            // when
            var result = mockMvc.perform(put("/api/order-tables/{orderTableId}/clear", orderTableId))
                    // then
                    .andExpect(status().isOk())
                    .andReturn();

            var response = objectMapper.readValue(result.getResponse().getContentAsString(), OrderTable.class);
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(orderTableId);
        }
    }
}
