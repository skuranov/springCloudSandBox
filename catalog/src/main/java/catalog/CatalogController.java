package catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


@RestController
public class CatalogController {

    @Autowired
    private Map<String, Product> inMemoryProductStorageById;

    @Autowired
    private Map<String, List<Product>> inMemoryProductStorageBySku;

    @RequestMapping("/")
    public ResponseEntity index(@RequestParam(name = "uniq_id", required = false) String uniqId, @RequestParam(required = false) String sku) {
        if (uniqId != null) {
            return new ResponseEntity(inMemoryProductStorageById.get(uniqId), HttpStatus.OK);
        }
        if (sku != null) {
            return new ResponseEntity(inMemoryProductStorageBySku.get(sku), HttpStatus.OK);
        }
        return null;
    }
}
