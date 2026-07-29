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
}