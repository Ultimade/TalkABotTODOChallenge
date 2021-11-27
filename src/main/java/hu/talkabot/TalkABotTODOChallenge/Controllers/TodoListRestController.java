package hu.talkabot.TalkABotTODOChallenge.Controllers;

import hu.talkabot.TalkABotTODOChallenge.Models.Api.RestResponse;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Services.TodoListServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/todo", name = "TODO")
public class TodoListRestController {

    private final TodoListServiceImpl todoListService;

    public TodoListRestController(TodoListServiceImpl todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping(value = "/api/listTodos")
    public RestResponse listTodo() {
        return new RestResponse("", "", todoListService.getTodoListDtoList());
    }

    @PostMapping(value = "/api/createTodo")
    public RestResponse createTodo(@RequestBody TodoListDto todoListDto ){
        return todoListService.createOrUpdateTodoList(todoListDto);
    }

    @PostMapping(value = "/api/editTodo")
    public RestResponse editTodo(@RequestBody TodoListDto todoListDto ){
        return todoListService.createOrUpdateTodoList(todoListDto);
    }

    @PostMapping(value = "/api/deleteTodo")
    public RestResponse deleteTodo(@RequestBody TodoListDto todoListDto ){
        return todoListService.deleteTodoList(todoListDto);
    }
}
