package hu.talkabot.TalkABotTODOChallenge;

import hu.talkabot.TalkABotTODOChallenge.Controllers.TodoListRestController;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Services.TodoListService;
import hu.talkabot.TalkABotTODOChallenge.Services.TodoListServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.datatables.mapping.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TalkABotTodoChallengeApplicationTests {

	@Autowired
	TodoListServiceImpl todoListService;

	@Autowired
	private TodoListRestController controller;

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@DisplayName("Testing TODO creation")
	@Test
	public void createTodoTest(){
		TodoListDto todoListDto = new TodoListDto();
		todoListDto.setName("Teszt1");
		todoListDto.setCreatedDate("2021-11-30");
		todoListDto.setPriority("LOW");
		TodoListRestController todoListRestController = new TodoListRestController(todoListService);

		System.out.println("create todo test");
		assertEquals(todoListRestController.createTodo(todoListDto).getType(), "success");
	}


	@DisplayName("Test list TODOs")
	@Test
	public void listTodos(){
		DataTablesInput input = new DataTablesInput();
		input.setDraw(1);
		input.setStart(0);
		input.setLength(20);
		List<Order> orders = new ArrayList<>();
		Order  order = new Order();
		order.setColumn(0);
		order.setDir("desc");
		orders.add(order);
		input.setOrder(orders);
		List<Column> columns = new ArrayList<>();
		for (int i = 0; i<6; i++){

			Column c = new Column();
			c.setName("");
			c.setSearchable(true);
			c.setOrderable(true);
			Search s = new Search();
			s.setRegex(false);
			s.setValue("");
			c.setSearch(s);
			switch (i){
				case 0 :
				case 5 :
					c.setData("id");
				c.setSearchable(false);
				c.setOrderable(false);
					break;
				case 1 : c.setData("name");
					break;
				case 2 : c.setData("createdDate");
					break;
				case 3 : c.setData("priority");
					break;
				case 4 : c.setData("deadline");
					break;
			}
			columns.add(c);
		}
		input.setColumns(columns);
		input.setSearchPanes(null);

		TodoListRestController todoListRestController = new TodoListRestController(todoListService);

		DataTablesOutput<TodoListDto> todoTable = todoListRestController.listTodo(input);

		System.out.println("List todos test");
		assertEquals(null, todoTable.getError());

		List<TodoListDto> todoListDtoList = todoTable.getData();
		if (!todoListDtoList.isEmpty()){
			TodoListDto todoListDto = todoListDtoList.get(0);

			System.out.println("update todo test");
			updateTodoTest(todoListDto);

			System.out.println("delete todo test");
			deleteTodoTest(todoListDto);
		}else{
			createTodoTest();
			listTodos();
		}


	}

	public void updateTodoTest(TodoListDto todoListDto){
		todoListDto.setName("Teszt13");
		todoListDto.setCreatedDate("2022-10-30");
		todoListDto.setPriority("HIGH");
		TodoListRestController todoListRestController = new TodoListRestController(todoListService);

		assertEquals(todoListRestController.createTodo(todoListDto).getType(), "success");
	}

	public void deleteTodoTest(TodoListDto todoListDto){

		TodoListRestController todoListRestController = new TodoListRestController(todoListService);

		assertEquals(todoListRestController.deleteTodo(todoListDto).getType(), "success");
	}

}
