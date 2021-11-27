package hu.talkabot.TalkABotTODOChallenge.Services;

import hu.talkabot.TalkABotTODOChallenge.Models.Api.RestResponse;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;

import java.util.List;

public interface TodoListService {

    public List<TodoListDto> getTodoListDtoList();

    public RestResponse createOrUpdateTodoList(TodoListDto todoListDto);

    public RestResponse deleteTodoList(TodoListDto todoListDto);
}
