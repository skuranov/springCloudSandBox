package product;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getDefaultProductInventoryByCode",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5")
            })
    @LoadBalanced
    @GetMapping("/product/uniqId/{uniqId}")
    public ResponseEntity byId(@PathVariable("uniqId") String uniqId) {
        Map<String, Boolean> availMap = restTemplate.getForObject("http://inventory-service/?uniq_ids=" + uniqId, Map.class);
        if (!availMap.get(uniqId)) {
            return new ResponseEntity("null", HttpStatus.OK);
        } else {
            return new ResponseEntity(restTemplate.getForObject("http://catalog-service/?uniq_id=" + uniqId, Product.class), HttpStatus.OK);
        }
    }

    @HystrixCommand(fallbackMethod = "getDefaultProductInventoryByCode",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5")
            })
    @LoadBalanced
    @GetMapping("/product/sku/{sku}")
    public ResponseEntity bySku(@PathVariable("sku") String sku) {
        List<Product> products = Arrays.asList(restTemplate.getForObject("http://catalog-service/?sku=" + sku, Product[].class));

        List<String> uniqIds = products.stream().map(x -> x.getUniqId()).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder().append("http://inventory-service/?uniq_ids=");
        uniqIds.forEach(x -> sb.append(x).append(","));
        Map<String, Boolean> availIdMap = restTemplate.getForObject(sb.toString(), Map.class);

        Set<String> availIds = availIdMap.entrySet()
                .stream()
                .filter(x -> x.getValue())
                .map(x -> x.getKey())
                .collect(Collectors.toSet());

        return new ResponseEntity(products.stream().filter(x -> availIds.contains(x.getUniqId())), HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    private ResponseEntity getDefaultProductInventoryByCode(String parameter) {
        return new ResponseEntity("Unfortunately, something very bad happened ", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
