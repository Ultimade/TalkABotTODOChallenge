package hu.talkabot.TalkABotTODOChallenge.Models.Dtos;

import hu.talkabot.TalkABotTODOChallenge.Enums.Priority;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TodoListDto {

    private Long id;

    private String name;

    private String deadline;

    private String createdDate;

    private String priority;
}
