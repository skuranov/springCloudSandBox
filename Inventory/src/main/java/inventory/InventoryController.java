package inventory;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@RestController
public class InventoryController {

    @Autowired
    private Map<String, Boolean> inMemoryInventoryStorageById;

    @RequestMapping("/")
    public ResponseEntity index(@RequestParam(name = "uniq_ids", required = false) String uniqIds) throws JsonProcessingException, InterruptedException {
        List<String> ids = Arrays.asList(uniqIds.split(","));
        sleep(500);
        return new ResponseEntity(
                ids.stream().collect(Collectors.toMap(id -> id, id -> inMemoryInventoryStorageById.get(id))), HttpStatus.OK);
    }
}
