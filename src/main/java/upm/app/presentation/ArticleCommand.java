package upm.app.presentation;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.Table;
import upm.app.data.models.Article;
import upm.app.presentation.view.ReflectiveTableBuilder;
import upm.app.services.ArticleService;

import java.math.BigDecimal;

@ShellComponent
@ShellCommandGroup("Shop: Article Commands")
public class ArticleCommand {

    private final ArticleService articleService;
    private final ValidationHandler validationHandler;

    public ArticleCommand(ArticleService articleService, ValidationHandler validationHandler) {
        this.articleService = articleService;
        this.validationHandler = validationHandler;
    }

    @ShellMethod("Creates a new article")
    public Article createArticle(@ShellOption(value = {"-n", "--name"}) String name,
                                 @ShellOption(value = {"-p", "--price"}) BigDecimal price,
                                 @ShellOption(value = {"-c", "--category"}) String category) {
        Article article = Article.builder()
                .name(name)
                .price(price)
                .category(category)
                .build();
        this.validationHandler.validate(article);
        return this.articleService.create(article);
    }

    @ShellMethod("Reads an article by ID")
    public Article readArticle(@ShellOption(value = {"-i", "--id"}) Integer id) {
        return this.articleService.read(id);
    }

    @ShellMethod("Deletes an article by ID")
    public void deleteArticle(@ShellOption(value = {"-i", "--id"}) Integer id) {
        this.articleService.delete(id);
    }

    @ShellMethod("Lists all articles")
    public Table listArticles() {
        return new ReflectiveTableBuilder<>(this.articleService.findAll())
                .buildShellTable();
    }
}
