package entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersGen")
    @SequenceGenerator(name = "usersGen", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    private String nickname;
    //private email email;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "user")
    private List<Reservation> reservations;

    public Users(String nickname) {
        this.nickname = nickname;
    }
}
