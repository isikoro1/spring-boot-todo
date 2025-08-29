package com.isikoro1.spring_boot_todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
	private final TodoRepository repository;
	
	// コンストラクタインジェクション（Springが自動でRepositoryを渡してくれる）
	public TodoController(TodoRepository repository) {
		this.repository = repository;
	}
	
	// 一覧取得（GET /todos）
	@GetMapping
	public List<Todo> findAll(){
		return repository.findAll();
	}
	
	// １件取得（GET　/todos/{id}）
	@GetMapping("/{id}")
	public ResponseEntity<Todo> findById(@PathVariable Long id) {
		return repository.findById(id)
				.map(ResponseEntity::ok)						// 見つかった → 200 OK
				.orElse(ResponseEntity.notFound().build());		// 見つからない → 404 Not Found
	}
	
	// 新規作成（POST /todos）
	@PostMapping
	public Todo create(@RequestBody Todo todo) {
		return repository.save(todo);
	}
	
	// 更新（PUT /todos/{id}）
	@PutMapping("/{id}")
	public Todo update(@PathVariable Long id, @RequestBody Todo updated) {
		updated.setId(id);
		return repository.save(updated);
	}
	
	// 削除（DELETE /todos/{id}）
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return ResponseEntity.noContent().build(); // 204 No Content
		} else {
			return ResponseEntity.notFound().build(); // 404 Not Found
		}
	}

}
