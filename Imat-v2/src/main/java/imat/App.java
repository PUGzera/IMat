package imat;

import imat.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Properties;


@SpringBootApplication
public class App extends Application {

    private Parent root;

    @Override
    public void init() throws Exception {
        SpringApplication applicationContext = new SpringApplicationBuilder(getClass()).web(WebApplicationType.NONE).build();
        Properties properties = new Properties();
        properties.setProperty("spring.jpa.hibernate.ddl-auto","update");
        properties.setProperty("spring.datasource.driverClassName", "org.h2.Driver");
        properties.setProperty("spring.datasource.password", "test");
        properties.setProperty("spring.datasource.username", "test");
        properties.setProperty("spring.datasource.url", "jdbc:h2:file:./db;DB_CLOSE_ON_EXIT=FALSE");
        applicationContext.setDefaultProperties(properties);
        ConfigurableApplicationContext context = applicationContext.run();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/default_view3.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        root = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
