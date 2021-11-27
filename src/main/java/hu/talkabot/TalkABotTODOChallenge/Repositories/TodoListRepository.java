package hu.talkabot.TalkABotTODOChallenge.Repositories;

import hu.talkabot.TalkABotTODOChallenge.Models.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    Optional<TodoList> findById(Long Id);
}