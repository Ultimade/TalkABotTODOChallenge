package hu.talkabot.TalkABotTODOChallenge.Services;

import hu.talkabot.TalkABotTODOChallenge.Enums.Priority;
import hu.talkabot.TalkABotTODOChallenge.Mapers.TodoListMapper;
import hu.talkabot.TalkABotTODOChallenge.Models.Api.RestResponse;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.ToTodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Models.TodoList;
import hu.talkabot.TalkABotTODOChallenge.Repositories.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.mapping.Search;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TodoListServiceImpl implements TodoListService {
    private final String ERROR = "error";
    private final String SUCCESS = "success";

    @Autowired
    TodoListRepository todoListRepository;

    /**
     * shorting and filtering to-do entities
     * @param input contains all necessary data for querying data's
     * @return A special list which contains the result of query
     */
    @Override
    public DataTablesOutput<TodoListDto> getTodoListDtoList(DataTablesInput input) {
        String createdDate = input.getColumn("createdDate").getSearch().getValue();

        input.getColumn("createdDate").setSearch(new Search("",false));
        ArrayList<LocalDateTime> createdDateArray = new ArrayList<>();

        if(createdDate!="")
        {
            String[] splittedDates = createdDate.split(" - ");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            createdDateArray.add(LocalDateTime.parse(splittedDates[0], formatter));
            createdDateArray.add(LocalDateTime.parse(splittedDates[1], formatter));
        }

        Specification<TodoList> additionalSpecification = new Specification<TodoList>() {

            List<Predicate> predicates = new ArrayList<>();

            @Override
            public Predicate toPredicate(Root<TodoList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                if(createdDateArray.size()==2)
                {
                    predicates.add(cb.and(cb.between(root.get("createdDate"), createdDateArray.get(0),createdDateArray.get(1))));
                    return cb.and(predicates.toArray(new Predicate[0]));
                }
                else
                {
                    return cb.and();
                }
            }
        };

        ToTodoListDto toTodoListDto = new ToTodoListDto();
        return todoListRepository.findAll(input, additionalSpecification , null, toTodoListDto);
    }

    /**
     * multi usage method which create new entity if it's not exist or update if it's exist
     * @param todoListDto contains to-do params like name, priority and deadline
     * @return a global response which tells what happened
     */
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
            e.printStackTrace();
            return new RestResponse("Creating or editing TODO finished with error! Error:"+ Arrays.toString(e.getStackTrace()), ERROR);
        }
    }

    /**
     * extend of createOrUpdateTodoList method functionality
     * @param todoListDto request param from api call
     * @param todoList request param converted to entity object
     * @return the final entity object
     */
    private TodoList updateTodolistByDto(TodoListDto todoListDto, TodoList todoList){
        try{

            todoList.setName(todoListDto.getName());
            if (todoListDto.getDeadline()!= null && !Objects.equals(todoListDto.getDeadline(), "")){
                todoList.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(todoListDto.getDeadline()));
            }
            todoList.setPriority(Priority.valueOf(todoListDto.getPriority()));
            if(todoList.getCreatedDate() == null){
                todoList.setCreatedBy("asd");
                todoList.setCreatedDate(new Date());
            }else{
                todoList.setModifiedBy("asd");
                todoList.setUpdatedDate(new Date());
            }
            return todoList;
        }catch(Exception e){
            e.printStackTrace();
            return todoList;
        }
    }


    /**
     * delete specific entity by id
     * @param id the id of an entity
     * @return  a global response which tells what happened
     */
    @Override
    public RestResponse deleteTodoList(Long id) {
        try {
            todoListRepository.deleteById(id);
            return new RestResponse("Deleting TODO finished!", SUCCESS);
        }catch (Exception e) {
            return new RestResponse(e.getMessage(), ERROR);
        }
    }

    @Override
    public TodoListDto getTodoListDtoById(Long id) {

        TodoListMapper mapper = new TodoListMapper();
        Optional<TodoList> todoOpt = todoListRepository.findById(id);
        if (todoOpt.isPresent() ){
            return mapper.convertToddoListEntityToDto(todoOpt.get());
        }
        return new TodoListDto();

    }

}
