package inventory;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

    @Autowired
    private YAMLConfig myConfig;

    @Autowired
    private DataSource csvDataSource;

    @Bean
    public DataSource csvDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.relique.jdbc.csv.CsvDriver");
        dataSource.setUrl("jdbc:relique:csv:" + myConfig.getPathToSourceCSV() + "?" +
                "separator=," + "&" + "fileExtension=.csv");
        return dataSource;
    }

    @Bean
    public Map<String, Boolean> inMemoryProductStorageById() {
        JdbcTemplate template = new JdbcTemplate(csvDataSource);
        String query = "SELECT uniq_id FROM " + myConfig.getNameOfSourceCSV();
        List<String> uniqIds = template.query(query, new StringMapper());
        return  uniqIds.stream().collect(Collectors.toMap(x -> x, y -> generateRandomBoolean()));
    }

    private static boolean generateRandomBoolean() {
        return Math.random() < 0.5;
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

    public static class StringMapper implements RowMapper<String> {
        @Override
        public String mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getString(1);
        }
    }
}
