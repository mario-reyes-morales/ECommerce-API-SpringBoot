package upm.app.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import upm.app.data.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
