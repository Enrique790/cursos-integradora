package mx.edu.utez.integradora;

import mx.edu.utez.integradora.category.model.Category;
import mx.edu.utez.integradora.category.model.CategoryDto;
import mx.edu.utez.integradora.category.model.CategoryRepository;
import mx.edu.utez.integradora.category.service.CategoryService;
import mx.edu.utez.integradora.course.model.Course;
import mx.edu.utez.integradora.course.model.CourseDto;
import mx.edu.utez.integradora.course.model.CourseRepository;
import mx.edu.utez.integradora.course.service.CourseService;
import mx.edu.utez.integradora.utils.ResponseObject;
import mx.edu.utez.integradora.utils.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class IntegradoraApplicationTests {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CategoryService categoryService;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // 1.
    void categoriaValidaTest() {
        CategoryDto categoryDto = new CategoryDto(null, null);
        categoryDto.setName("CategoriaFea");
        categoryDto.setDescription("Categoría para pruebas feas");

        when(repository.searchByNameAndId("categoriafea", 0L)).thenReturn(Optional.empty());
        when(repository.saveAndFlush(any(Category.class)))
                .thenReturn(new Category("categoriafea", "Categoría para pruebas feas", true));

        ResponseEntity<Object> response = service.save(categoryDto);
        ResponseObject responseObject = (ResponseObject) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals("Se registro la categoria con exito", responseObject.getMessage());
    }

    @Test // 2.
    void nombreConNombreCortoTest() {
        CategoryDto categoryDto = new CategoryDto(null, null);
        categoryDto.setName("XD");
        categoryDto.setDescription("Categoria pero mala");

        ResponseEntity<Object> response = service.save(categoryDto);
        ResponseObject responseObject = (ResponseObject) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Type.WARN, responseObject.getType());
        assertEquals("El nombre de la categoria debe de tener mas de 3 caracteres", responseObject.getMessage());
    }

    @Test // 3.
    void categoriaDuplicadaTest() {
        CategoryDto categoryDto = new CategoryDto(null, null);
        categoryDto.setName("CategoriaD");
        categoryDto.setDescription("Copia de categoria");

        when(repository.searchByNameAndId("categoriad", 0L))
            .thenReturn(Optional.of(new Category("categoriad", "Descripción", true)));

        ResponseEntity<Object> response = service.save(categoryDto);
        ResponseObject responseObject = (ResponseObject) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Type.WARN, responseObject.getType());
        assertEquals("El nombre de la categoria ya existe", responseObject.getMessage());
    }

    @Test // 4.
    void listarTodasLasCategoriasTest() {
        List<Category> categories = Arrays.asList(
                new Category("Categoria1", "Descripción 1", true),
                new Category("Categoria2", "Descripción 2", true)
        );

        when(repository.findAll()).thenReturn(categories);

        ResponseEntity<Object> response = service.findAll();
        ResponseObject responseObject = (ResponseObject) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals("Listado de categorias", responseObject.getMessage());
        assertEquals(categories, responseObject.getData());
    }

    @Test // 5.
    void categoriasActivasTest() {
        List<Category> activeCategories = Arrays.asList(
                new Category("CategoriaActiva1", "Descripción 1", true),
                new Category("CategoriaActiva2", "Descripción 2", true)
        );

        when(repository.findAllByStatusOrderByName(true)).thenReturn(activeCategories);

        ResponseEntity<Object> response = service.findAllActive();
        ResponseObject responseObject = (ResponseObject) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals("Listado de categorias activas", responseObject.getMessage());
        assertEquals(activeCategories, responseObject.getData());
    }

    @Test // 6.
    void actualizarCategoriaExistenteTest() {
        CategoryDto dto = new CategoryDto("categoria valida", "descripcion");
        dto.setId(1L);

        Category category = new Category("Categoria Activa", "Descripcion", true);
        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.saveAndFlush(any(Category.class))).thenReturn(category);

        ResponseEntity<Object> response = service.changeStatus(dto);
        ResponseObject responseObject = (ResponseObject) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Se cambio el estado de la categoria con exito", responseObject.getMessage());
        verify(repository, times(1)).saveAndFlush(any(Category.class));
    }

    @Test // 7.
    void shouldNotUpdateNonExistentCategory() {
        long nonExistentCategoryId = 121212L;
        CategoryDto categoryDto = new CategoryDto(null, null);
        categoryDto.setId(nonExistentCategoryId);
        categoryDto.setName("Categoria Esquizofrenica");
        categoryDto.setDescription("No existe");

        when(repository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.update(categoryDto);
        ResponseObject responseObject = (ResponseObject) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Type.WARN, responseObject.getType());
        assertEquals("No se encontro esta categoria", responseObject.getMessage());
    }

    @Test // 8.
    void cambiarEstadoCategoriaTest() {
        long categoryId = 1L;
        CategoryDto categoryDto = new CategoryDto(null, null);
        categoryDto.setId(categoryId);

        Category existingCategory = new Category("Categoria Activa", "Descripcion", true);
        when(repository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(repository.saveAndFlush(any(Category.class))).thenReturn(existingCategory);

        ResponseEntity<Object> response = service.changeStatus(categoryDto);
        ResponseObject responseObject = (ResponseObject) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals("Se cambio el estado de la categoria con exito", responseObject.getMessage());
    }

    @Test // 9.
    void cambiarEstadoCategoriaNoExistenteTest() {
        long nonExistentCategoryId = 777L;
        CategoryDto categoryDto = new CategoryDto(null, null);
        categoryDto.setId(nonExistentCategoryId);

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = service.changeStatus(categoryDto);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("No se encontro esta categoria", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 10.
    void eliminarCategoriaExistenteTest() {
        long existentCategoryId = 1L;
        Category category = new Category();
        category.setId(existentCategoryId);

        when(repository.findById(anyLong())).thenReturn(Optional.of(category));
        doNothing().when(repository).delete(category);

        ResponseEntity<Object> responseEntity = service.delete(existentCategoryId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Se eliminó la categoría con éxito", responseObject.getMessage());
        assertEquals(Type.SUCCESS, responseObject.getType());
    }

    @Test // 11.
    void eliminarCategoriaNoExistente() {
        long nonExistentCategoryId = 666L;

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = categoryService.delete(nonExistentCategoryId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("No se encontro esta categoria", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 12.
    void nombreEnMinusculas() {
        String originalName = "CATEgoria RAra";
        String expectedName = "categoria rara";
        CategoryDto categoryDto = new CategoryDto(expectedName, expectedName);
        categoryDto.setName(originalName);
        categoryDto.setDescription("Prueba Mayusculas");

        Category savedCategory = new Category();
        savedCategory.setName(expectedName);

        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(savedCategory);
        when(categoryRepository.searchByNameAndId(any(String.class), any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = categoryService.save(categoryDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Se registro la categoria con exito", responseObject.getMessage());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals(expectedName, savedCategory.getName());
    }

    @Test // 13.
    void descripcionCategoriaObligatoria() {
        CategoryDto categoryDto = new CategoryDto(null, null);
        categoryDto.setName("Categoria sin Desc");
        categoryDto.setDescription("");

        when(categoryRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("Es necesaria una descripción"));

        ResponseEntity<Object> responseEntity = categoryService.save(categoryDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Es necesaria una descripción", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 14.
    void nombreCategoriaObligatoria() {
        CategoryDto categoryDto = new CategoryDto("", "no tengo nombre");
        categoryDto.setName(""); // Nombre vacío
        categoryDto.setDescription("No tengo nombre");

        when(categoryRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("Es necesario un nombre"));

        ResponseEntity<Object> responseEntity = categoryService.save(categoryDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Es necesario un nombre", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 15. 
    void guardarCategoriaConEstadoPredeterminado() {
        CategoryDto categoryDto = new CategoryDto("status predeterminado", "mira mi status");
        categoryDto.setName("Status predeterminado");
        categoryDto.setDescription("Mira mi status");

        Category savedCategory = new Category();
        savedCategory.setName("status predeterminado");
        savedCategory.setDescription("Mira mi status");
        savedCategory.setStatus(true); // Estado predeterminado

        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(savedCategory);
        when(categoryRepository.searchByNameAndId(any(String.class), any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = categoryService.save(categoryDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Se registro la categoria con exito", responseObject.getMessage());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals(true, savedCategory.isStatus());
    }

    @Test // 16.
    void buscarCategoríaPorNombreYID() {
        String categoryName = "Categoria Fea";
        long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        when(categoryRepository.searchByNameAndId(anyString(), anyLong())).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.searchByNameAndId(categoryName, categoryId);

        assertTrue(result.isPresent());
        assertTrue(result.get().getName().equals(categoryName));
        assertTrue(result.get().getId() == categoryId);
    }

    @Test // 17.
    void buscarCategoríaQueNoExiste() {
        String categoryName = "Categoria no existente";
        long categoryId = 117L;

        when(categoryRepository.searchByNameAndId(anyString(), anyLong())).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.searchByNameAndId(categoryName, categoryId);

        assertFalse(result.isPresent());
    }

    @Test // 18.
    void CursoConNombreCorto() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("A"); // Nombre corto
        courseDto.setDuration("3 semanas");
        courseDto.setSyllabus("Temario SN");
        courseDto.setDescription("Curso pero corto");

        when(courseRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("El nombre debe tener más de 3 caracteres"));

        ResponseEntity<Object> responseEntity = courseService.createNew(courseDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El nombre debe tener más de 3 caracteres", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 19.
    void CrearCursoValido () {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Curso P");
        courseDto.setDuration("4 semanas");
        courseDto.setSyllabus("Temario de prueba");
        courseDto.setDescription("Curso pero bueno");

        Course savedCourse = new Course();
        savedCourse.setName("Curso P");
        savedCourse.setDuration("4 semanas");
        savedCourse.setSyllabus("Temario de prueba");
        savedCourse.setDescription("Curso pero bueno");

        when(courseRepository.saveAndFlush(any(Course.class))).thenReturn(savedCourse);
        when(courseRepository.searchByNameAndId(any(String.class), any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = courseService.createNew(courseDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Se registro el curso con exito", responseObject.getMessage());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals("Curso P", savedCourse.getName());
    }

    @Test // 20.
    void CrearCursoSinNombre() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName(""); // Nombre vacío
        courseDto.setDuration("3 semanas");
        courseDto.setSyllabus("Temario SN");
        courseDto.setDescription("Curso pero sin nombre");

        when(courseRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("El nombre es necesario"));

        ResponseEntity<Object> responseEntity = courseService.createNew(courseDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El nombre es necesario", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 21.
    void CrearCursoConNombreMuyLargo() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("CursoConNombreMuyLargoYnoSeQuePonerAAAAAAAAAAAA"); // Nombre largo
        courseDto.setDuration("5 semanas");
        courseDto.setSyllabus("Temario Largo");
        courseDto.setDescription("Curso pero con nombre muy largo XD");

        when(courseRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("El nombre no puede ser mayor de 40 caracteres"));

        ResponseEntity<Object> responseEntity = courseService.createNew(courseDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El nombre no puede ser mayor de 40 caracteres", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 22.
    void ListarTodosLosCursos() {
        List<Course> courses = Arrays.asList(
            new Course("Curso 1", "4 semanas", "Temario 1", "Descripción 1"),
            new Course("Curso 2", "3 semanas", "Temario 2", "Descripción 2")
        );

        when(courseRepository.findAll()).thenReturn(courses);

        ResponseEntity<Object> responseEntity = courseService.allCourses();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals(courses, responseObject.getData());
        assertEquals(Type.SUCCESS, responseObject.getType());
    }

    @Test // 23.
    void ListarCursosActivos() {
        List<Course> activeCourses = Arrays.asList(
            new Course("Curso Activo 1", "4 semanas", "Temario Activo 1", "Descripción Activa 1", true),
            new Course("Curso Activo 2", "3 semanas", "Temario Activo 2", "Descripción Activa 2", true)
        );

        when(courseRepository.findAllByStatusTrue()).thenReturn(activeCourses);

        ResponseEntity<Object> responseEntity = courseService.statusTrue();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals(activeCourses, responseObject.getData());
        assertEquals(Type.SUCCESS, responseObject.getType());
    }

    @Test // 24.
    void ObtenerCursoEspecífico() {
        long courseId = 3L;
        Course course = new Course("Curso Especifico", "6 semanas", "Temario Especifico", "Descripción Especifica");
        course.setId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        ResponseEntity<Object> responseEntity = courseService.specificCourse(courseId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals(course, responseObject.getData());
        assertEquals(Type.SUCCESS, responseObject.getType());
    }

    @Test // 25.
    void ObtenerCursoInexistente() {
        long courseId = 321L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = courseService.specificCourse(courseId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El curso no existe", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 26.
    void ActualizarCursoValido() {
        long courseId = 1L;
        CourseDto courseDto = new CourseDto();
        courseDto.setId(courseId);
        courseDto.setName("Curso para Actualizar");
        courseDto.setDuration("8 Semanas");
        courseDto.setSyllabus("Temario feo");
        courseDto.setDescription("Curso pero para actualizar");

        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        existingCourse.setName("Curso Original");
        existingCourse.setDuration("4 Semanas");
        existingCourse.setSyllabus("Temario original");
        existingCourse.setDescription("Curso original");

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(existingCourse));
        when(courseRepository.saveAndFlush(any(Course.class))).thenReturn(existingCourse);

        ResponseEntity<ResponseObject> responseEntity = courseService.updateCourse(courseDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El curso se actualizó con éxito", responseObject.getMessage());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals("Curso para Actualizar", existingCourse.getName());
        assertEquals("8 Semanas", existingCourse.getDuration());
        assertEquals("Temario feo", existingCourse.getSyllabus());
        assertEquals("Curso pero para actualizar", existingCourse.getDescription());
    }

    @Test // 27.
    void ActualizarCursoInexistente() {
        long courseId = 888L;
        CourseDto courseDto = new CourseDto();
        courseDto.setId(courseId);
        courseDto.setName("Curso Halucinogeno");
        courseDto.setDuration("3 Semanas");
        courseDto.setSyllabus("Temario que no existe");
        courseDto.setDescription("Curso que no existe");

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ResponseObject> responseEntity = courseService.updateCourse(courseDto);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El curso no existe", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 28.
    void CambiarEstadoDeCurso() {
        long courseId = 1L;
        Course activeCourse = new Course("Curso Activo", "6 semanas", "Temario Activo", "Descripción Activa");
        activeCourse.setId(courseId);
        activeCourse.setStatus(true);

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(activeCourse));
        when(courseRepository.saveAndFlush(any(Course.class))).thenReturn(activeCourse);

        ResponseEntity<ResponseObject> responseEntity = courseService.changeStatus(courseId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El estado del curso se cambió con éxito", responseObject.getMessage());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals(false, activeCourse.isStatus()); 
    }

    @Test // 29.
    void CambiarEstadoDeCursoInexistente() {
        long courseId = 999L;

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ResponseObject> responseEntity = courseService.changeStatus(courseId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El curso no existe", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 30.
    void ValidarCursoSinTemario() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Curso sin Tem");
        courseDto.setDuration("6 Semanas");
        courseDto.setDescription("Curso sin temario feo");
        courseDto.setSyllabus(""); // Temario vacío

        when(courseRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("Es necesario un temario"));

        ResponseEntity<Object> responseEntity = courseService.createNew(courseDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Es necesario un temario", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 31.
    void ValidarCursoSinDuración() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Curso sin Duracion");
        courseDto.setSyllabus("Temario feo");
        courseDto.setDescription("Curso pero sin duracion");
        courseDto.setDuration(""); // Duración vacía

        when(courseRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("Se necesita una duración"));

        ResponseEntity<Object> responseEntity = courseService.createNew(courseDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Se necesita una duración", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test //32.
    void CrearCursoConDescripciónMuyLarga() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Curso Largo");
        courseDto.setDuration("9 semanas");
        courseDto.setSyllabus("Temario largo");
        courseDto.setDescription("Descripccion larga que no va a ser aceptada porque es fea y no se que escribir");

        when(courseRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("La descripción no puede ser mayor de 40 caracteres"));

        ResponseEntity<Object> responseEntity = courseService.createNew(courseDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("La descripción no puede ser mayor de 40 caracteres", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 33.
    void CambiarEstadoDeCursoActivoAInactivo() {
        long courseId = 2L;
        Course activeCourse = new Course("Curso Activo", "6 semanas", "Temario Activo", "Descripción Activa");
        activeCourse.setId(courseId);
        activeCourse.setStatus(true);

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(activeCourse));
        when(courseRepository.saveAndFlush(any(Course.class))).thenReturn(activeCourse);

        ResponseEntity<ResponseObject> responseEntity = courseService.changeStatus(courseId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El estado del curso se cambió con éxito", responseObject.getMessage());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals(false, activeCourse.isStatus());
    }

    @Test // 34.
    void CambiarEstadoDeCursoInactivoAActivo() {
        long courseId = 2L;
        Course inactiveCourse = new Course("Curso Inactivo", "6 semanas", "Temario Inactivo", "Descripción Inactiva");
        inactiveCourse.setId(courseId);
        inactiveCourse.setStatus(false);

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(inactiveCourse));
        when(courseRepository.saveAndFlush(any(Course.class))).thenReturn(inactiveCourse);

        ResponseEntity<ResponseObject> responseEntity = courseService.changeStatus(courseId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("El estado del curso se cambió con éxito", responseObject.getMessage());
        assertEquals(Type.SUCCESS, responseObject.getType());
        assertEquals(true, inactiveCourse.isStatus());
    }

    @Test // 35.
    void CrearCursoConCategoriaNula() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Curso sin Categoria");
        courseDto.setDuration("5 semanas");
        courseDto.setSyllabus("Temario feo");
        courseDto.setDescription("Curso pero sin categoria");
        courseDto.setCategory(null);

        when(courseRepository.saveAndFlush(any())).thenThrow(new IllegalArgumentException("Se necesita una categoría"));

        ResponseEntity<Object> responseEntity = courseService.createNew(courseDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = (ResponseObject) responseEntity.getBody();
        assertEquals("Se necesita una categoría", responseObject.getMessage());
        assertEquals(Type.WARN, responseObject.getType());
    }

    @Test // 36.
    void ValidarCamposVaciosEnCurso() {
        
    }
}
