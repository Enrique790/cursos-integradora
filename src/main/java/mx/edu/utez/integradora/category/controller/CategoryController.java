package mx.edu.utez.integradora.category.controller;

import mx.edu.utez.integradora.category.model.CategoryDto;

import mx.edu.utez.integradora.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/active")
    public ResponseEntity<Object> findAllEnabled() {
        return categoryService.findAllActive();
    }

    @PostMapping("/save")
    public ResponseEntity<Object> save(
            @Validated({ CategoryDto.Register.class }) @RequestBody CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(
            @Validated({ CategoryDto.Modify.class }) @RequestBody CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    // @PutMapping
    // public ResponseEntity<Object>
    // changeStatus(@Validated({CategoryDto.Modify.class}) @RequestBody CategoryDto
    // categoryDto) {
    // return categoryService.changeStatus(categoryDto);
    // }
}
