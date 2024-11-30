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
        return new ResponseEntity<>(new ResponseObject(repository.findAll(), Type.SUCCESS, "Listado de categorias"), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAllActive(){
        return new ResponseEntity<>(new ResponseObject(repository.findAllByStatusOrderByName(true), Type.SUCCESS, "Listado de categorias activas"), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Object> save(CategoryDto dto) {
        dto.setName(dto.getName().toLowerCase());
        if(dto.getName().length() < 3) {
            return new ResponseEntity<>(new ResponseObject("El nombre de la categoria debe de tener mas de 3 caracteres",Type.WARN), HttpStatus.BAD_REQUEST);
        }
        Optional<Category> optionalCategory = repository.searchByNameAndId(dto.getName(), 0L);
        if(optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ResponseObject("El nombre de la categoria ya existe", Type.WARN), HttpStatus.BAD_REQUEST);
        }
        Category category = new Category(dto.getName(), dto.getDescription(), true);
        category = repository.saveAndFlush(category);
        if (category == null) {
            log.error("Error al guardar la categoria");
            return new ResponseEntity<>(new ResponseObject("Error al guardar la categoria", Type.ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseObject("Se registro la categoria con exito", Type.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> update(CategoryDto dto) {
        dto.setName(dto.getName().toLowerCase());
        if(dto.getName().length() < 3) {
            return new ResponseEntity<>(new ResponseObject("El nombre de la categoria debe de tener as de 3 caracteres",Type.WARN), HttpStatus.BAD_REQUEST);
        }
        Optional<Category> optional = repository.findById(dto.getId());
        if(!optional.isPresent()) {
            return new ResponseEntity<>(new ResponseObject("No se encontro esta categoria", Type.WARN), HttpStatus.NOT_FOUND);
        }
        Optional<Category> optionalCategory = repository.searchByNameAndId(dto.getName(), dto.getId());
        if(optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ResponseObject("El nombre de la categoria ya existe", Type.WARN), HttpStatus.BAD_REQUEST);
        }
        Category category = optional.get();
        category.setName(dto.getName());
        category = repository.saveAndFlush(category);
        if (category == null) {
            log.error("No se puedo actualizar la categoria");
            return new ResponseEntity<>(new ResponseObject("Error al actualizar la categoria", Type.ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseObject("Se actualizar la categoria con exito", Type.SUCCESS), HttpStatus.OK);
    }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> changeStatus(CategoryDto dto) {
        Optional<Category> optionalCategory = repository.findById(dto.getId());
        if(!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ResponseObject("No se encontro esta categoria", Type.WARN), HttpStatus.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        category.setStatus(!category.isStatus());
        category = repository.saveAndFlush(category);
        if (category == null) {
            log.error("No se pudo cambiar el estado de la categoria");
            return new ResponseEntity<>(new ResponseObject("Error al cambiar el estado de la categoria", Type.ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseObject("Se cambio el estado de la categoria con exito", Type.SUCCESS), HttpStatus.OK);
    }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> delete(Long id) {
        Optional<Category> optionalCategory = repository.findById(id);
        if(!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ResponseObject("No se encontro esta categoria", Type.WARN), HttpStatus.NOT_FOUND);
        }
        try{
            repository.deleteById(id);
            return new ResponseEntity<>(new ResponseObject("Se ha eliminado la categoia correctacmente", Type.SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al eliminar la categoria", e);
            return new ResponseEntity<>(new ResponseObject("No se pudo eliinar la categoria", Type.ERROR), HttpStatus.BAD_REQUEST);
        }
    }
}
