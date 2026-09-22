package upm.app.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import upm.app.data.models.Article;
import upm.app.data.models.Order;
import upm.app.data.models.User;
import upm.app.data.repositories.ArticleRepository;
import upm.app.data.repositories.OrderRepository;
import upm.app.data.repositories.UserRepository;
import upm.app.services.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCreateOrder() {
        User user = this.userRepository
                .save(User.builder().email("order.test@example.com").name("Order Tester").build());
        Article article = this.articleRepository
                .save(Article.builder().name("Test Article").price(new BigDecimal("10.00")).build());

        Order order = this.orderService.create(user.getId(), article.getId(), 2);

        assertNotNull(order.getId());
        assertEquals(user.getId(), order.getOwner().getId());
        assertEquals(1, order.getLines().size());
        assertEquals(new BigDecimal("20.00"), order.getTotalPrice());
    }

    @Test
    void testCreateOrderUserNotFound() {
        Article article = this.articleRepository
                .save(Article.builder().name("Test Article 2").price(new BigDecimal("10.00")).build());
        assertThrows(NotFoundException.class, () -> this.orderService.create(99999, article.getId(), 1));
    }

    @Test
    void testCreateOrderArticleNotFound() {
        User user = this.userRepository
                .save(User.builder().email("order.test2@example.com").name("Order Tester 2").build());
        assertThrows(NotFoundException.class, () -> this.orderService.create(user.getId(), 99999, 1));
    }

    @Test
    void testFindByUser() {
        User user = this.userRepository.save(User.builder().email("finder@example.com").name("Finder").build());
        Article article = this.articleRepository.save(Article.builder().name("Findable").price(BigDecimal.TEN).build());
        this.orderService.create(user.getId(), article.getId(), 1);

        List<Order> orders = this.orderService.findByUser(user.getId());
        assertFalse(orders.isEmpty());
        assertEquals(user.getId(), orders.get(0).getOwner().getId());
    }
}
