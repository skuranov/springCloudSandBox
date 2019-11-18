package catalog;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();
        product.setUniqId(resultSet.getString("uniq_id"));
        product.setSku(resultSet.getString("sku"));
        product.setNameTitle(resultSet.getString("name_title"));
        product.setDescription(resultSet.getString("description"));
        product.setListPrice(resultSet.getString("list_price"));
        product.setSalePrice(resultSet.getString("sale_price"));
        product.setCategory(resultSet.getString("category"));
        product.setCategoryTree(resultSet.getString("category_tree"));
        product.setAverageProductRating(resultSet.getString("average_product_rating"));
        product.setProductUrl(resultSet.getString("product_url"));
        product.setBrand(resultSet.getString("brand"));
        product.setTotalNumberReviews(resultSet.getString("total_number_reviews"));
        product.setProductImageUrls(resultSet.getString("product_image_urls"));
        product.setReviews(resultSet.getString("Reviews"));
        return product;
    }
}
