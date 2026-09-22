package upm.app.presentation;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.Table;
import upm.app.data.models.User;
import upm.app.presentation.view.ReflectiveTableBuilder;
import upm.app.services.UserService;

@ShellComponent
@ShellCommandGroup("Shop: Users Commands")
public class UserCommand {
    private final UserService userService;
    private final ValidationHandler validationHandler;

    public UserCommand(UserService userService, ValidationHandler validationHandler) {
        this.userService = userService;
        this.validationHandler = validationHandler;
    }

    // shell:>help
    // shell:>help create-user
    // shell:>create-user -h
    // shell:>create-user --email blablabla@gmail.com --name juan
    // shell:>create-user -n juan -e blablabla@gmail.com
    @ShellMethod(value = "Se crea un usuario")
    public User createUser(@ShellOption(value = {"-e", "--email"}) String email,
                           @ShellOption(value = {"-n", "--name"}) String name) {
        User user = User.builder().email(email).name(name).build();
        this.validationHandler.validate(user);
        return this.userService.create(user);
    }

    @ShellMethod("Lista todos los usuarios")
    public Table listUsers() {
        return new ReflectiveTableBuilder<>(this.userService.findAll().toList()).buildShellTable();
    }

}
