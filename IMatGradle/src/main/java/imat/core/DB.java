package imat.core;

import imat.entities.Product;
import imat.entities.ProductRepo;
import imat.user.User;
import imat.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DB implements ApplicationRunner {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Product> productList = new ArrayList<>();
        Path path = Paths.get(getClass().getClassLoader().getResource("cache/products.txt").toURI());
        List<String> products = Files.readAllLines(path);
        products.forEach(s -> productRepo.save(parser(s)));
        User user = new User("test@test.se", "12345678", "199910024040", "hej", "då", "0708545454", "test", "42249","test");
        userRepo.save(user);
    }

    private Product parser(String string) {
        String[] data = string.split(";");
        return new Product(Integer.valueOf(data[0]), data[1], data[2],false, Double.valueOf(data[3]), data[4]);
    }
}
