package upm.app.services;

import org.springframework.stereotype.Service;
import upm.app.data.models.Article;
import upm.app.data.repositories.ArticleRepository;
import upm.app.services.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public Article create(Article article) {
        article.setRegistrationDate(LocalDate.now());
        return this.repository.save(article);
    }

    public Article read(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article with ID " + id + " does not exist"));
    }

    public void delete(Integer id) {
        Article articleToDelete = this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article with ID " + id + " does not exist"));

        this.repository.deleteById(id);
    }

    public List<Article> findAll() {
        return this.repository.findAll();
    }
}
