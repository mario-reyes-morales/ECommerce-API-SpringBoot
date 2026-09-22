package upm.app.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "shopping_line", joinColumns = @JoinColumn(name = "order_id"))
    private List<ShoppingLine> lines = new ArrayList<>();

    private BigDecimal totalPrice = BigDecimal.ZERO;
    private boolean discount;

    public Order(User owner) {
        this.owner = owner;
        this.lines = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
        this.discount = false;
    }

    public void addProduct(Article article, Integer quantity) {
        ShoppingLine line = ShoppingLine.of(article, quantity);
        this.lines.add(line);
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (ShoppingLine line : lines) {
            total = total.add(line.getPrice());
        }
        this.totalPrice = total;
        setDiscount();
        applyDiscount();
    }

    private void setDiscount() {
        this.discount = this.totalPrice.compareTo(new BigDecimal("100")) > 0;
    }

    private void applyDiscount() {
        if (discount) {
            this.totalPrice = this.totalPrice.multiply(new BigDecimal("0.95"));
        }
    }

    @Override
    public String toString() {
        StringBuilder items = new StringBuilder();
        for (ShoppingLine line : lines) {
            Article art = line.getProduct();
            items.append("\n   - ")
                    .append(art.getName())
                    .append(" [Categoria: ").append(art.getCategory())
                    .append(", Precio unitario: ").append(art.getPrice())
                    .append("] x ").append(line.getQuantity())
                    .append(" = ").append(line.getPrice())
                    .append(" (Descuento línea: ").append(line.isDiscount())
                    .append(")");
        }

        return "Order{" +
                "\n owner=" + this.owner.getName() +
                ",\n totalPrice=" + this.totalPrice +
                ",\n discount=" + this.discount +
                ",\n items=[" + items + "\n ]" +
                "\n}";
    }
}
