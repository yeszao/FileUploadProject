package ae.teletronics.gary.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@Tag(name = "Home")
@Validated
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> home() {

        return ResponseEntity.ok("welcome");
    }

}