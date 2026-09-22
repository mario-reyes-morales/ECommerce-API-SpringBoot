package upm.app.data.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data //@ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appUser") // conflict with user table
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true, nullable = false)
    private String email;
    @Size(min = 3, max = 20)
    private String name;
}