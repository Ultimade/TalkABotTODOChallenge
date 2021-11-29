package hu.talkabot.TalkABotTODOChallenge.Controllers;

import hu.talkabot.TalkABotTODOChallenge.Enums.Priority;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Services.TodoListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    private final TodoListService todoListService;

    public DefaultController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping(value = {"/create-todo-dialog"})
    public String addCasteDialog(ModelMap model) {
        model.addAttribute("priorityList", getPriorityList());
        return "todo/modals/addOrEditTodo";
    }
    @GetMapping(value = {"/edit-todo-dialog/{id}"})
    public String editTodoDialog(ModelMap model, @PathVariable("id") Long id) {
        model.addAttribute("priorityList", getPriorityList());
        model.addAttribute("todo", todoListService.getTodoListDtoById(id));
        TodoListDto asd =todoListService.getTodoListDtoById(id);
        return "todo/modals/addOrEditTodo";
    }

    private List<String> getPriorityList(){
        List<String> priorityList = new ArrayList<>();
        priorityList.add(Priority.LOW.getPriority());
        priorityList.add(Priority.MEDIUM.getPriority());
        priorityList.add(Priority.HIGH.getPriority());
        return priorityList;
    }

    @GetMapping(value = {"/todo_list", "/todoList"})
    public String todoList() {
        return "todo/list";
    }
}
