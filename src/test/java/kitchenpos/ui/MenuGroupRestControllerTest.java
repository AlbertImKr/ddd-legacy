package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import kitchenpos.application.MenuGroupService;
import kitchenpos.config.TestConfig;
import kitchenpos.domain.MenuGroup;
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
@WebMvcTest(MenuGroupRestController.class)
@Import(TestConfig.class)
class MenuGroupRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuGroupService menuGroupService;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("메뉴 그룹이 생성에 실패하면 400 Bad Request를 응답한다.")
    @Test
    void create_menu_group_if_failed_then_responds_400_bad_request() throws Exception {
        // given
        var name = "";
        var body = new HashMap<String, Object>() {{
            put("name", name);
        }};
        var content = objectMapper.writeValueAsString(body);
        given(menuGroupService.create(any())).willThrow(new IllegalArgumentException());

        // when
        mockMvc.perform(
                        post("/api/menu-groups")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                // then
                .andExpect(status().isBadRequest());
    }


    @DisplayName("메뉴 그룹이 생성에 성공하면 201 Created를 응답한다.")
    @Test
    void create_menu_group_if_succeeds_then_responds_201_created() throws Exception {
        // given
        var menuGroup = new MenuGroup();
        var name = "치킨";
        var body = new HashMap<String, Object>() {{
            put("name", name);
        }};
        var content = objectMapper.writeValueAsString(body);
        given(menuGroupService.create(any())).willReturn(menuGroup);

        // when
        mockMvc.perform(
                        post("/api/menu-groups")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                // then
                .andExpect(status().isCreated());
    }
}
