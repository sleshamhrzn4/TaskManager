package com.taskmanager.dao;

import com.taskmanager.model.TaskModel;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskDAOTest {

    private TaskDAO taskDAO;

    @BeforeAll
    static void setUpDatabaseProperties() {
        System.setProperty("db.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        System.setProperty("db.driver", "org.h2.Driver");
    }

    @BeforeEach
    void setUp() throws Exception {
        taskDAO = new TaskDAO();

        try (Connection con = com.taskmanager.utils.DBConfig.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS tasks");
            String schema = new String(Files.readAllBytes(
                    Paths.get("src/test/resources/schema.sql")));
            stmt.execute(schema);
        }
    }

    @Test
    void addTask_thenRetrievedById_matchesInsertedFields() throws Exception {
        TaskModel task = new TaskModel();
        task.setUserId(1);
        task.setTitle("Write report");
        task.setDescription("Quarterly report");
        task.setPriority("High");
        task.setStatus("todo");
        task.setCreatedDate(LocalDateTime.now());
        task.setDueDate(LocalDate.now().plusDays(3));

        taskDAO.addTask(task);

        List<TaskModel> tasks = taskDAO.getAllTaskByUser(1);
        assertEquals(1, tasks.size());
        assertEquals("Write report", tasks.get(0).getTitle());
        assertEquals("High", tasks.get(0).getPriority());
    }

    @Test
    void getAllTaskByUser_returnsEmptyList_whenNoTasksExist() throws Exception {
        List<TaskModel> tasks = taskDAO.getAllTaskByUser(99);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void deleteTask_removesRow() throws Exception {
        TaskModel task = new TaskModel();
        task.setUserId(1);
        task.setTitle("Temp task");
        task.setStatus("todo");
        task.setCreatedDate(LocalDateTime.now());
        taskDAO.addTask(task);

        List<TaskModel> before = taskDAO.getAllTaskByUser(1);
        int taskId = before.get(0).getTaskId();

        taskDAO.deleteTask(taskId);

        assertNull(taskDAO.getTaskById(taskId));
    }

    private void insertSampleTasks() throws Exception{
        TaskModel t1 = new TaskModel();
        t1.setUserId(1);
        t1.setTitle("Alpha task");
        t1.setDescription("first");
        t1.setPriority("Low");
        t1.setStatus("todo");
        t1.setCreatedDate(LocalDateTime.now());
        t1.setDueDate(LocalDate.now().minusDays(2));
        taskDAO.addTask(t1);

        TaskModel t2 = new TaskModel();
        t2.setUserId(1);
        t2.setTitle("Beta task");
        t2.setDescription("second");
        t2.setPriority("High");
        t2.setStatus("done");
        t2.setCreatedDate(LocalDateTime.now());
        t2.setDueDate(LocalDate.now().minusDays(1));
        taskDAO.addTask(t2);

        TaskModel t3 = new TaskModel();
        t3.setUserId(1);
        t3.setTitle("Gamma task");
        t3.setDescription("third");
        t3.setPriority("Medium");
        t3.setStatus("todo");
        t3.setCreatedDate(LocalDateTime.now());
        t3.setDueDate(LocalDate.now().plusDays(5));
        taskDAO.addTask(t3);
    }

    @Test
    void getTasksPaged_sortByTitleAscending() throws Exception{
        insertSampleTasks();

        List<TaskModel> result = taskDAO.getTasksPaged(1,null, "all", "all" ,false,"title","ASC" ,1,10);
        assertEquals(3,result.size());
        assertEquals(3, result.size());
        assertEquals("Alpha task", result.get(0).getTitle());
        assertEquals("Beta task", result.get(1).getTitle());
        assertEquals("Gamma task", result.get(2).getTitle());


    }

    @Test
    void getTasksPaged_filterByPriority() throws Exception{
        insertSampleTasks();

        List<TaskModel> result = taskDAO.getTasksPaged(1,null, "High", "all", false, "title","ASC", 1,10);
        assertEquals(1, result.size());
        assertEquals("Beta task", result.get(0).getTitle());
    }

    @Test
    void getTasksPaged_filtersByOverdueObly() throws Exception{
        insertSampleTasks();

        List<TaskModel> result = taskDAO.getTasksPaged(1,null,"all", "all" ,true, "title", "ASC", 1,10);
        assertEquals(1, result.size());
        assertEquals("Alpha task", result.get(0).getTitle());
    }

    @Test
    void getTasksPaged_respectsPageSize() throws Exception {
        insertSampleTasks();

        List<TaskModel> page1 = taskDAO.getTasksPaged(
                1, null, "all", "all", false, "title", "ASC", 1, 2);

        assertEquals(2, page1.size());
        assertEquals("Alpha task", page1.get(0).getTitle());
        assertEquals("Beta task", page1.get(1).getTitle());
    }

    @Test
    void getTotalTaskCount_matchesInsertedCount() throws Exception {
        insertSampleTasks();

        int count = taskDAO.getTotalTaskCount(1, null, "all", "all", false);

        assertEquals(3, count);
    }

    }
