package kitchenpos.util;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.Menu;

public class MenuBuilder {

    private UUID id;
    private String name;
    private BigDecimal price;
    private boolean displayed;

    private MenuBuilder() {
    }

    public static MenuBuilder id(UUID id) {
        MenuBuilder menuBuilder = new MenuBuilder();
        menuBuilder.id = id;
        return menuBuilder;
    }

    public MenuBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public MenuBuilder displayed(boolean displayed) {
        this.displayed = displayed;
        return this;
    }

    public Menu build() {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setDisplayed(displayed);
        return menu;
    }
}
