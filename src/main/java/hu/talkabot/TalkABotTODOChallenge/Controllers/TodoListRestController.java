package hu.talkabot.TalkABotTODOChallenge.Controllers;

import hu.talkabot.TalkABotTODOChallenge.Models.Api.RestResponse;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Models.TodoList;
import hu.talkabot.TalkABotTODOChallenge.Services.TodoListServiceImpl;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/todo", name = "TODO")
public class TodoListRestController {

    private final TodoListServiceImpl todoListService;

    public TodoListRestController(TodoListServiceImpl todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping(value = "/api/listTodos")
    @ResponseBody
    public DataTablesOutput<TodoListDto> listTodo(@Valid @RequestBody DataTablesInput input) {
        //return todoListService.getTodoListDtoList(input);
        return todoListService.getTodosCriteria(input);
    }


    /**
     * rest endpoint for creating and editing to-do.
     * @param todoListDto json request object wich contains to-do datas
     * @return a global response which tells what happened
     */
    @PostMapping(value = "/api/createOrEditTodo")
    public RestResponse createTodo(@RequestBody TodoListDto todoListDto ){
        return todoListService.createOrUpdateTodoList(todoListDto);
    }

    /**
     * rest endpoint for delete to-do
     */
    @PostMapping(value = "/api/deleteTodo")
    public RestResponse deleteTodo(@RequestBody TodoListDto todoListDto){
        return todoListService.deleteTodoList(todoListDto.getId());
    }
}
