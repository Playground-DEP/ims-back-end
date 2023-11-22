package lk.ijse.dep11.ims.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.ims.to.CoursesTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/courses")

public class CourseHttpController {

    private final HikariDataSource pool;
    public CourseHttpController() {
        HikariConfig config = new HikariConfig();
        config.setUsername("root");
        config.setPassword("Ravindu123");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/dep11_ims");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("maximumPoolSoze",10);
        pool = new HikariDataSource(config);
    }
    @PreDestroy
    public void destroy(){
        pool.close();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public CoursesTO createCourses(@RequestBody @Validated CoursesTO courses){
        try(Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("INSERT INTO course(name, duration_in_months) VALUES (?,?)"
                    , Statement.RETURN_GENERATED_KEYS);
            stm.setString(1,courses.getName());
            stm.setInt(2,courses.getDurationMonths());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            courses.setId(id); // api hadan nathi eke generate key value eka
            return courses;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{courseId}", consumes = "application/json")
    public void updateCourses(@PathVariable int courseId,
                                @RequestBody @Validated CoursesTO courses){
        try (Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM course WHERE id=?");
            stmExist.setInt(1,courseId);
            if(!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task Not Found"); //404
            }
            PreparedStatement stm = connection.prepareStatement("UPDATE course SET name=?,duration_in_months=? WHERE id=?");
            stm.setString(1,courses.getName());
            stm.setInt(2,courses.getDurationMonths());
            stm.setInt(3,courseId);
            stm.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{courseId}")
    public void deleteCourses(@PathVariable int courseId){
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection
                    .prepareStatement("SELECT * FROM course WHERE id = ?");
            stmExist.setInt(1, courseId);
            if (!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("DELETE FROM course WHERE id=?");
            stm.setInt(1, courseId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{courseId}",produces = "application/json")
    public CoursesTO getCourseDetail(@PathVariable int courseId){
        try (Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM course WHERE id=?");
            stm.setInt(1,courseId);
            ResultSet rst = stm.executeQuery();
            if(!stm.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            CoursesTO courses = new CoursesTO();
            while (rst.next()){
                courses.setId(rst.getInt("id"));
                courses.setName(rst.getString("name"));
                courses.setDurationMonths(rst.getInt("durationMonths"));
            }
            return courses;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping(produces = "application/json")
    public List<CoursesTO> getAllCourses(){
        try(Connection connection = pool.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM course ORDER BY id");
            List<CoursesTO> coursesList = new LinkedList<>();
            while (rst.next()){
                int id = rst.getInt("id");
                String name = rst.getString("name");
                int duration = rst.getInt("durationMonths");
                coursesList.add(new CoursesTO(id, name, duration));
            }
            return coursesList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
