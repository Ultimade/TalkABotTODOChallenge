package hu.talkabot.TalkABotTODOChallenge.Models.Dtos;

import hu.talkabot.TalkABotTODOChallenge.Mapers.TodoListMapper;
import hu.talkabot.TalkABotTODOChallenge.Models.TodoList;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor
public class ToTodoListDto  implements Function<TodoList, TodoListDto> {

    @Override
    public TodoListDto apply(TodoList todoList){
        TodoListMapper mapper = new TodoListMapper();
        return mapper.convertToddoListEntityToDto(todoList);
    }

}
