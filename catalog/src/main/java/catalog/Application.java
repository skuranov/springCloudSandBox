package catalog;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

    @Autowired
    private YAMLConfig myConfig;

    @Autowired
    private DataSource csvDataSource;

    private Map<String, Product> inMemoryProductStorageById;

    @Bean
    public DataSource csvDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.relique.jdbc.csv.CsvDriver");
        dataSource.setUrl("jdbc:relique:csv:" + myConfig.getPathToSourceCSV() + "?" +
                "separator=," + "&" + "fileExtension=.csv");
        return dataSource;
    }

    @Bean
    public Map<String, Product> inMemoryProductStorageById() {
        JdbcTemplate template = new JdbcTemplate(csvDataSource);
        String query = "SELECT uniq_id,sku,name_title,description,list_price,sale_price,category,category_tree,average_product_rating,product_url,product_image_urls,brand,total_number_reviews,Reviews FROM " + myConfig.getNameOfSourceCSV();
        List<Product> products = template.query(query, new ProductMapper());
        inMemoryProductStorageById = products.stream().collect(Collectors.toMap(Product::getUniqId, y -> y));
        return inMemoryProductStorageById;
    }

    @Bean
    public Map<String, List<Product>> inMemoryProductStorageBySku() {
        return inMemoryProductStorageById.values().stream().collect(Collectors.groupingBy(Product::getSku));
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}
