package hu.talkabot.TalkABotTODOChallenge.Mapers;


import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Models.TodoList;

import java.util.ArrayList;
import java.util.List;

public class TodoListMapper extends BaseMapper {

    public TodoListDto convertToddoListEntityToDto(TodoList todoList) {
        return modelMapper.map(todoList, TodoListDto.class);
    }

    public TodoList convertTodoListDtoToEntity(TodoListDto todoListDto) {
        return modelMapper.map(todoListDto, TodoList.class);
    }

    public List<TodoList> convertDtoListToEntityList(List<TodoListDto> todoListDtoList){
        List<TodoList> todoList = new ArrayList<>();
        for(TodoListDto todoListDto : todoListDtoList){
            todoList.add(convertTodoListDtoToEntity(todoListDto));
        }
        return todoList;
    }

    public List<TodoListDto> convertEntityListToDtoList(List<TodoList> todoList){
        List<TodoListDto> todoListDtoList = new ArrayList<>();
        for(TodoList todo : todoList){
            todoListDtoList.add(convertToddoListEntityToDto(todo));
        }
        return todoListDtoList;
    }
}
