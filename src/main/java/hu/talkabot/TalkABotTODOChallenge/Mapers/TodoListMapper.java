package hu.talkabot.TalkABotTODOChallenge.Mapers;


import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Models.TodoList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TodoListMapper extends BaseMapper {

    public TodoListDto convertToddoListEntityToDto(TodoList todoList) {

        SimpleDateFormat formatOfDate = new SimpleDateFormat("yyyy-MM-dd");
        TodoListDto todoListDto = modelMapper.map(todoList, TodoListDto.class);
        todoListDto.setCreatedDate(formatOfDate.format(todoList.getCreatedDate()));
        if (todoList.getDeadline() != null){
            todoListDto.setDeadline(formatOfDate.format(todoList.getDeadline()));
        }
        return todoListDto;
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
