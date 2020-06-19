package springfoxdemo.boot.swagger.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import springfox.petstore.model.Pet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
  @RequestMapping(value = "/category/Resource", method = RequestMethod.GET)
  public ResponseEntity<String> search(@RequestParam(value = "someEnum") Category someEnum) {
    return ResponseEntity.ok(someEnum.name());
  }

  @RequestMapping(value = "/category/map", method = RequestMethod.GET)
  public Map<String, Map<String, Pet>> map() {
    return new HashMap<>();
  }

  @RequestMapping(value = "/category/{id}", method = RequestMethod.POST)
  public ResponseEntity<Void> someOperation(
      @PathVariable long id,
      @RequestBody int userId) {
    return ResponseEntity.ok(null);
  }

  @RequestMapping(value = "/category/{id}/{userId}", method = RequestMethod.POST)
  public ResponseEntity<Void> ignoredParam(
      @PathVariable long id,
      @PathVariable @ApiIgnore int userId) {
    return ResponseEntity.ok(null);
  }

  @RequestMapping(value = "/category/{id}/map", method = RequestMethod.POST)
  public ResponseEntity<Void> map(
      @PathVariable String id,
      @RequestParam Map<String, String> test) {
    return ResponseEntity.ok(null);
  }

  @RequestMapping(value = "/categories", method = RequestMethod.POST)
  public ResponseEntity<List<Category>> map(String[] categories) {
    return ResponseEntity.ok(null);
  }
}
