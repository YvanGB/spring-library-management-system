package com.esmt.lms.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esmt.lms.model.Category;
import com.esmt.lms.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category Management", description = "APIs for managing categories")
@RestController
@RequestMapping(value = "/rest/category")
public class CategoryRestController {
	
	@Autowired
	private CategoryService categoryService;

	@Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
			@ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
			@ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
	})
	@GetMapping(value = {"/", "/list"})
	public List<Category> all() {
		return categoryService.getAll();
	}

}
