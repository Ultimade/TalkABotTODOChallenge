package hu.talkabot.TalkABotTODOChallenge.Repositories;

import hu.talkabot.TalkABotTODOChallenge.Enums.Priority;
import hu.talkabot.TalkABotTODOChallenge.Mapers.TodoListMapper;
import hu.talkabot.TalkABotTODOChallenge.Models.Dtos.TodoListDto;
import hu.talkabot.TalkABotTODOChallenge.Models.TodoList;
import hu.talkabot.TalkABotTODOChallenge.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.mapping.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
public class TodoListCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public List<TodoList> getAllTodos(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TodoList> cr = cb.createQuery(TodoList.class);
        Root<TodoList> root = cr.from(TodoList.class);
        cr.select(root);

        Query query = session.createQuery(cr);
        List<TodoList> results = query.getResultList();
        return results;
    }

    public DataTablesOutput<TodoListDto> getTodosByCriterias(DataTablesInput input){
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TodoList> cr = cb.createQuery(TodoList.class);
        Root<TodoList> root = cr.from(TodoList.class);
        cr.select(root).where(cb.like(root.get("name"), "%"+input.getColumn("name").getSearch().getValue()+"%"));
        if (!Objects.equals(input.getColumn("priority").getSearch().getValue(), ""))
        cr.select(root).where(cb.equal(root.get("priority"), Priority.valueOf(input.getColumn("priority").getSearch().getValue())));


        String createdDate = input.getColumn("createdDate").getSearch().getValue();
        ArrayList<Date> createdDateArray = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        if(!Objects.equals(createdDate, ""))
        {
            String[] splittedDates = createdDate.split(" - ");

            try{

                createdDateArray.add(formatter.parse(splittedDates[0]));
                createdDateArray.add(new Date(formatter.parse(splittedDates[1]).getTime() + TimeUnit.DAYS.toMillis(1)));

                Predicate geFrom = cb.greaterThanOrEqualTo(root.get("createdDate"), createdDateArray.get(0));
                Predicate ltTo = cb.lessThan(root.get("createdDate"), createdDateArray.get(1));

                cr.select(root).where(cb.and(geFrom, ltTo));

            }catch (Exception e){
                e.printStackTrace();
            }
        }


        String deadline = input.getColumn("deadline").getSearch().getValue();

        ArrayList<Date> deadlineArray = new ArrayList<>();

        if(!Objects.equals(deadline, ""))
        {
            String[] splittedDates = deadline.split(" - ");
            try {
                deadlineArray.add(formatter.parse(splittedDates[0]));
                deadlineArray.add(new Date(formatter.parse(splittedDates[1]).getTime() + TimeUnit.DAYS.toMillis(1)));

                Predicate geFrom = cb.greaterThanOrEqualTo(root.get("deadline"), deadlineArray.get(0));
                Predicate ltTo = cb.lessThan(root.get("deadline"), deadlineArray.get(1));

                cr.select(root).where(cb.and(geFrom, ltTo));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<javax.persistence.criteria.Order> orderList = new ArrayList();

        for (Order order : input.getOrder()) {

            if (order.getDir().equals("desc")){
                orderList.add(cb.desc(root.get(getColName(order.getColumn()))));
            }else{
                orderList.add(cb.asc(root.get(getColName(order.getColumn()))));
            }
        }

        cr.orderBy(orderList);

        cr.select(root);

        Query query = session.createQuery(cr);
        int resultsCount = query.getResultList().size();
        query.setFirstResult(input.getStart());
        query.setMaxResults(input.getLength());
        List results = query.getResultList();


        DataTablesOutput<TodoListDto> responseDto = new DataTablesOutput<>();
        responseDto.setDraw(input.getDraw());

        TodoListMapper mapper = new TodoListMapper();
        responseDto.setData(mapper.convertEntityListToDtoList(results));
        responseDto.setRecordsFiltered(resultsCount);
        responseDto.setRecordsTotal(resultsCount);
        return responseDto;
    }

    public Boolean createTodo(TodoList todoList){

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Long todoID = null;

        try {
            tx = session.beginTransaction();
            todoID = (Long) session.save(todoList);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    private String getColName(int colId){
        switch (colId) {
            case 0: return "id";
            case 1: return "name";
            case 2: return "createdDate";
            case 3: return "priority";
            case 4: return "deadline";
            default: return "";
        }
    }

    public Boolean updateTodo(TodoList todoList){
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<TodoList> criteriaUpdate = cb.createCriteriaUpdate(TodoList.class);
        Root<TodoList> root = criteriaUpdate.from(TodoList.class);
        criteriaUpdate.set("name", todoList.getName());
        criteriaUpdate.set("priority", todoList.getPriority());
        criteriaUpdate.set("deadline", todoList.getDeadline());
        criteriaUpdate.where(cb.equal(root.get("id"), todoList.getId()));


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery(criteriaUpdate).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public Boolean deleteTodoById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<TodoList> criteriaDelete = cb.createCriteriaDelete(TodoList.class);
        Root<TodoList> root = criteriaDelete.from(TodoList.class);
        criteriaDelete.where(cb.greaterThan(root.get("id"), id));

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery(criteriaDelete).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }
}
