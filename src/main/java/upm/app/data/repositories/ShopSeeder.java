package upm.app.data.repositories;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import upm.app.data.models.Article;
import upm.app.data.models.Order;
import upm.app.data.models.User;

import java.math.BigDecimal;
import java.util.Arrays;

@Repository
public class ShopSeeder {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ShopSeeder(UserRepository userRepository, OrderRepository orderRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.articleRepository = articleRepository;
        this.seed();
    }

    public void seed() {
        LogManager.getLogger(this.getClass()).info(() -> ">>>>>>>>>> Seed Data Base!!!");
        User[] users = {
                User.builder().name("Mario Reyes").email("mario.reyes@alumnos.upm.es").build(),
                User.builder().name("Alejandro Corona").email("alejandro.corona@alumnos.upm.es").build(),
        };
        this.userRepository.saveAll(Arrays.asList(users));

        Article[] articles = {
                Article.builder().name("Clean Code").price(new BigDecimal("29.99")).category("Libro").build(),
                Article.builder().name("Boligrafo UPM").price(new BigDecimal("1.50")).category("Papelería").build(),
                Article.builder().name("Camiseta UPM").price(new BigDecimal("14.90")).category("Ropa").build()
        };
        this.articleRepository.saveAll(Arrays.asList(articles));

        User user = this.userRepository.findById(1).get();
        Order order = new Order(user);
        Article boli = this.articleRepository.findById(2).get();
        order.addProduct(boli, 4);
        Article cleanCode = this.articleRepository.findById(1).get();
        order.addProduct(cleanCode, 1);

        this.orderRepository.save(order);

        User user2 = this.userRepository.findById(2).get();
        Order order2 = new Order(user2);
        order2.addProduct(boli, 4);
        Article camiseta = this.articleRepository.findById(3).get();
        order2.addProduct(camiseta, 1);

        this.orderRepository.save(order2);
    }
}