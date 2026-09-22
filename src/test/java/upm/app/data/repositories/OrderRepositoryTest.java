package upm.app.data.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import upm.app.data.models.Article;
import upm.app.data.models.Order;
import upm.app.data.models.User;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void testSaveAndFind() {
        User user = this.userRepository.save(User.builder().email("repo.test@example.com").name("Repo Tester").build());
        Order order = new Order(user);
        Article article = this.articleRepository
                .save(Article.builder().name("Repo Article").price(BigDecimal.TEN).build());
        order.addProduct(article, 1);

        Order savedOrder = this.orderRepository.save(order);

        assertTrue(this.orderRepository.findById(savedOrder.getId()).isPresent());
        assertEquals(user.getId(), savedOrder.getOwner().getId());
    }

    @Test
    void testFindByOwnerId() {
        User user = this.userRepository
                .save(User.builder().email("repo.owner@example.com").name("Owner Tester").build());
        Order order1 = new Order(user);
        this.orderRepository.save(order1);
        Order order2 = new Order(user);
        this.orderRepository.save(order2);

        List<Order> orders = this.orderRepository.findByOwnerId(user.getId());
        assertEquals(2, orders.size());
    }

    @Test
    void testFindDistinctByLinesProductId() {
        User user = this.userRepository
                .save(User.builder().email("repo.product@example.com").name("Product Tester").build());
        Article article = this.articleRepository
                .save(Article.builder().name("Shared Article").price(BigDecimal.TEN).build());

        Order order1 = new Order(user);
        order1.addProduct(article, 1);
        this.orderRepository.save(order1);

        Order order2 = new Order(user);
        order2.addProduct(article, 2);
        this.orderRepository.save(order2);

        List<Order> orders = this.orderRepository.findDistinctByLinesProductId(article.getId());
        assertEquals(2, orders.size());
    }
}
