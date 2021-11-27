package hu.talkabot.TalkABotTODOChallenge.Services;

import hu.talkabot.TalkABotTODOChallenge.Mapers.TodoListMapper;
import hu.talkabot.TalkABotTODOChallenge.Models.Api.RestResponse;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Models.TodoList;
import hu.talkabot.TalkABotTODOChallenge.Repositories.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TodoListServiceImpl implements TodoListService {
    private final String ERROR = "error";
    private final String SUCCESS = "success";

    @Autowired
    TodoListRepository todoListRepository;

    @Override
    public List<TodoListDto> getTodoListDtoList() {
        TodoListMapper mapper = new TodoListMapper();
        return mapper.convertEntityListToDtoList(todoListRepository.findAll());
    }

    @Override
    public RestResponse createOrUpdateTodoList(TodoListDto todoListDto) {
        try {
            TodoList todoList = new TodoList();
            if (todoListDto.getId()!= null && todoListDto.getId()!= 0){
                Optional<TodoList> todoOpt = todoListRepository.findById(todoListDto.getId());
                if (todoOpt.isPresent() ){
                    todoList = todoOpt.get();
                }
            }
            todoListRepository.save(updateTodolistByDto(todoListDto, todoList));
            return new RestResponse("Creating or editing TODO finished!", SUCCESS);
        }catch (Exception e) {
            return new RestResponse(e.getMessage(), ERROR);
        }
    }

    private TodoList updateTodolistByDto(TodoListDto todoListDto, TodoList todoList){
        todoList.setName(todoListDto.getName());
        todoList.setDeadline(todoListDto.getDeadline());
        todoList.setPriority(todoListDto.getPriority());
        if(todoList.getCreatedDate() == null){
            todoList.setCreatedBy("asd");
            todoList.setCreatedDate(new Date());
        }else{
            todoList.setModifiedBy("asd");
            todoList.setUpdatedDate(new Date());
        }
        return todoList;
    }

    @Override
    public RestResponse deleteTodoList(TodoListDto todoListDto) {
        try {
            todoListRepository.deleteById(todoListDto.getId());
            return new RestResponse("Deleting TODO finished!", SUCCESS);
        }catch (Exception e) {
            return new RestResponse(e.getMessage(), ERROR);
        }
    }

}
