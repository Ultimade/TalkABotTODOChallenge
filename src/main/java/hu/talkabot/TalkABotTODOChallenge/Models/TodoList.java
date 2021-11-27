package hu.talkabot.TalkABotTODOChallenge.Models;

import hu.talkabot.TalkABotTODOChallenge.Enums.Priority;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "table_todo")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TodoList extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "deadline")
    private Date deadline;

    @Column(name = "priority", nullable = false)
    private Priority priority;
}
