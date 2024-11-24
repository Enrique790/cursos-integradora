package mx.edu.utez.integradora.category.service;

import mx.edu.utez.integradora.category.model.Category;
import mx.edu.utez.integradora.category.model.CategoryDto;
import mx.edu.utez.integradora.category.model.CategoryRepository;
import mx.edu.utez.integradora.utils.ResponseObject;
import mx.edu.utez.integradora.utils.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAll(){
        return new ResponseEntity<>(new ResponseObject(repository.findAll(), Type.SUCCESS, "Listado de categorias activas"), HttpStatus.OK);
    }


}
