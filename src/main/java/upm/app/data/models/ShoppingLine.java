package upm.app.data.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingLine {
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Article product;

    private Integer quantity;
    private BigDecimal price;
    private boolean discount;

    public static ShoppingLine of(Article product, Integer quantity) {
        ShoppingLine line = new ShoppingLine();
        line.product = product;
        line.quantity = quantity;
        line.updateDerivedFields();
        return line;
    }

    public void updateDerivedFields() {
        updateDiscount();
        updatePrice();
    }

    private void updateDiscount() {
        this.discount = quantity != null && quantity >= 3;
    }

    private void updatePrice() {
        if (product == null || quantity == null) {
            this.price = BigDecimal.ZERO;
            return;
        }
        BigDecimal base = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        if (discount) {
            base = base.multiply(new BigDecimal("0.90"));
        }
        this.price = base;
    }
}
