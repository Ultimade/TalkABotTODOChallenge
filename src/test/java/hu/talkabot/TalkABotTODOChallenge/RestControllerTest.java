package hu.talkabot.TalkABotTODOChallenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.talkabot.TalkABotTODOChallenge.Controllers.TodoListRestController;
import hu.talkabot.TalkABotTODOChallenge.Models.Api.RestResponse;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Services.TodoListServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {TodoListRestController.class, TodoListServiceImpl.class})
@WebMvcTest
@ExtendWith(SpringExtension.class)
public class RestControllerTest {

    @MockBean
    TodoListServiceImpl todoListService;

    @Autowired
    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createTodo() throws Exception {

        TodoListDto todoListDto = new TodoListDto();
        todoListDto.setName("mákostészta");
        todoListDto.setCreatedDate("2021-11-30");
        todoListDto.setPriority("MEDIUM");

        RestResponse resp = new RestResponse("Creating or editing TODO finished!", "success");

        Mockito.when(todoListService.createOrUpdateTodoList(ArgumentMatchers.any())).thenReturn(resp);
        String json = mapper.writeValueAsString(todoListDto);
        mockMvc.perform(post("/todo/api/createOrEditTodo").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
                .andExpect(jsonPath("$.type", Matchers.equalTo("success")))
                .andExpect(jsonPath("$.msg", Matchers.equalTo("Creating or editing TODO finished!")));

    }
}
