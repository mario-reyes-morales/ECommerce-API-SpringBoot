package upm.app.services;

import org.springframework.stereotype.Service;
import upm.app.data.models.Article;
import upm.app.data.models.Order;
import upm.app.data.models.ShoppingLine;
import upm.app.data.models.User;
import upm.app.data.repositories.ArticleRepository;
import upm.app.data.repositories.OrderRepository;
import upm.app.data.repositories.UserRepository;
import upm.app.services.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Stream;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public OrderService(OrderRepository repository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public Order create(Integer userId, Integer articleId, Integer quantity) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));
        Article article = this.articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article with ID " + articleId + " not found"));

        Order order = new Order(user);
        order.addProduct(article, quantity);
        return this.repository.save(order);
    }

    public Order create(Order order) {
        return this.repository.save(order);
    }

    public List<Order> findByUser(Integer userId) {
        return this.repository.findByOwnerId(userId);
    }

    public List<Article> findArticlesByUser(Integer userId) {
        return this.findByUser(userId).stream()
                .flatMap(order -> order.getLines().stream())
                .map(ShoppingLine::getProduct)
                .distinct()
                .toList();
    }

    public List<Order> findByArticle(Integer articleId) {
        return repository.findDistinctByLinesProductId(articleId);
    }

    public List<User> findUserByArticle(Integer articleId) {
        return this.findByArticle(articleId).stream()
                .map(Order::getOwner)
                .distinct()
                .toList();
    }

    public Order read(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with ID " + id + " does not exist"));
    }

    public void delete(Integer id) {
        this.repository.deleteById(id);
    }

    public Stream<Order> findAll() {
        return this.repository.findAll().stream();
    }
}
