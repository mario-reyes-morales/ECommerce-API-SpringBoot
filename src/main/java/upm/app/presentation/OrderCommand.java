package upm.app.presentation;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import upm.app.data.models.Order;
import upm.app.services.OrderService;

import java.util.List;

@ShellComponent
@ShellCommandGroup("Shop: Order Commands")
public class OrderCommand {
    private final OrderService service;
    private final ValidationHandler validationHandler;

    public OrderCommand(OrderService service, ValidationHandler validationHandler) {
        this.service = service;
        this.validationHandler = validationHandler;
    }

    @ShellMethod("Crea una compra")
    public Order createOrder(@ShellOption(value = {"-i", "--id"}) Integer ownerId,
                             @ShellOption(value = {"-p", "--product"}) Integer productId,
                             @ShellOption(value = {"-q", "--quantity"}) Integer quantity) {
        return service.create(ownerId, productId, quantity);
    }

    @ShellMethod("Lista todas las compras de un usuario")
    public void listOrdersByUser(@ShellOption(value = {"-i", "--id"}) Integer ownerId) {
        this.showList("Orders for User " + ownerId, this.service.findByUser(ownerId));
    }

    @ShellMethod("Lista todas las compras de un mismo artículo")
    public void listOrdersByArticle(@ShellOption(value = {"-i", "--id"}) Integer productId) {
        this.showList("Orders", this.service.findByArticle(productId));
    }

    @ShellMethod("Lista todos las compras")
    public void listOrders() {
        this.showList("Orders", this.service.findAll().toList());
    }

    @ShellMethod("Borra una compra")
    public void deleteOrder(@ShellOption(value = {"-i", "--id"}) Integer id) {
        this.service.delete(id);
        System.out.println("Order " + id + " deleted");
    }

    @ShellMethod("Lee una compra")
    public void readOrder(@ShellOption(value = {"-i", "--id"}) Integer id) {
        System.out.println(this.service.read(id));
    }

    private <T> void showList(String title, List<T> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("No items available");
        } else {
            System.out.println(title);
            int index = 1;
            for (T item : items) {
                System.out.println(index + ". " + item.toString());
                index++;
            }
        }
    }
}
