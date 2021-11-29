package hu.talkabot.TalkABotTODOChallenge.Services;

import hu.talkabot.TalkABotTODOChallenge.Models.Api.RestResponse;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface TodoListService {

    DataTablesOutput<TodoListDto> getTodoListDtoList(DataTablesInput input);

    RestResponse createOrUpdateTodoList(TodoListDto todoListDto);

    RestResponse deleteTodoList(Long id);

    TodoListDto getTodoListDtoById (Long id);
}
