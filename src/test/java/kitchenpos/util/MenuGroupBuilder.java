package kitchenpos.util;

import kitchenpos.domain.MenuGroup;

public class MenuGroupBuilder {

    private String name;

    private MenuGroupBuilder() {
    }

    public static MenuGroupBuilder name(String name) {
        MenuGroupBuilder menuGroupBuilder = new MenuGroupBuilder();
        menuGroupBuilder.name = name;
        return menuGroupBuilder;
    }

    public MenuGroup build() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
