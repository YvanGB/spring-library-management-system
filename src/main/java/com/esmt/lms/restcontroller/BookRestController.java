package com.esmt.lms.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esmt.lms.model.Book;
import com.esmt.lms.model.Category;
import com.esmt.lms.service.BookService;
import com.esmt.lms.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Book Management", description = "APIs for managing books")
@RestController
@RequestMapping(value = "/rest/book")
public class BookRestController {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private CategoryService categoryService;


	@Operation(summary = "Get all books", description = "Retrieve a list of all books")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
			@ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
			@ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
	})
	@GetMapping(value = {"/", "/list"})
	public List<Book> all() {
		return bookService.getAll();
	}


	@Operation(summary = "Get books by category", description = "Retrieve a list of books by category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
			@ApiResponse(responseCode = "400", description = "Invalid category ID supplied"),
			@ApiResponse(responseCode = "404", description = "Category not found")
	})
	@GetMapping(value = "/{id}/list")
	public List<Book> get(@PathVariable(name = "id") Long id) {
		Category category = categoryService.get(id);
		return bookService.getByCategory( category );
	}


	@Operation(summary = "Get available books by category", description = "Retrieve a list of available books by category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
			@ApiResponse(responseCode = "400", description = "Invalid category ID supplied"),
			@ApiResponse(responseCode = "404", description = "Category not found")
	})
	@GetMapping(value = "/{id}/available")
	public List<Book> getAvailableBooks(@PathVariable(name = "id") Long id) {
		Category category = categoryService.get(id);
		return bookService.geAvailabletByCategory( category );
	}
	
}
