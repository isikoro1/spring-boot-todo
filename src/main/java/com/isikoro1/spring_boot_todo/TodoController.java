package com.isikoro1.spring_boot_todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/todos")
public class TodoController {
	
	private final TodoService service;

	public TodoController(TodoService service) {
		this.service = service;
	}
	
	// 一覧取得（GET /todos）
	@GetMapping
	public List<Todo> findAll(){
		return service.findAll();
	}
	
	// １件取得（GET　/todos/{id}）
	@GetMapping("/{id}")
	public ResponseEntity<Todo> findById(@PathVariable Long id) {
		return service.findById(id)
				.map(ResponseEntity::ok)						// 見つかった → 200 OK
				.orElse(ResponseEntity.notFound().build());		// 見つからない → 404 Not Found
	}
	
	// 新規作成（POST /todos）
	@PostMapping
	public Todo create(@Valid @RequestBody Todo todo) {
		return service.create(todo);
	}
	
	// 更新（PUT /todos/{id}）
	@PutMapping("/{id}")
	public Todo update(@PathVariable Long id, @Valid @RequestBody Todo updated) {
		return service.update(id, updated);
	}
	
	// 削除（DELETE /todos/{id}）
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (service.delete(id)) {
			return ResponseEntity.noContent().build(); 
		} else {
			return ResponseEntity.notFound().build(); 
		}
	}

}
