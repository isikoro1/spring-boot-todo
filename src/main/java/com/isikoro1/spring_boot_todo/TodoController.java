package com.isikoro1.spring_boot_todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/todos")
public class TodoController {
	
	private static final Logger log = LoggerFactory.getLogger(TodoController.class);
	
	private final TodoService service;

	public TodoController(TodoService service) {
		this.service = service;
	}
	
	// 一覧取得（GET /todos）
	@GetMapping
	public List<Todo> findAll(){
		log.info("全てのToDoを取得します");
		return service.findAll();
	}
	
	// １件取得（GET　/todos/{id}）
	@GetMapping("/{id}")
	public ResponseEntity<Todo> findById(@PathVariable Long id) {
		return service.findById(id)
				.map(ResponseEntity::ok)							// 見つかった → 200 OK
				.orElse(ResponseEntity.notFound().build());			// 見つからない → 404 Not Found
	}
	
	// 新規作成（POST /todos）
	@PostMapping
	public Todo create(@Valid @RequestBody Todo todo) {
		log.info("新規ToDo作成: {}", todo.getTitle()); // ← {}に値が埋め込まれる
		return service.create(todo);
	}
	
	// 更新（PUT /todos/{id}）
	@PutMapping("/{id}")
	public Todo update(@PathVariable Long id, @Valid @RequestBody Todo updated) {
		log.info("ToDo更新: id={}, title={}, done={}", id, updated.getTitle(), updated.isDone());
		return service.update(id, updated);
	}
	
	// 削除（DELETE /todos/{id}）
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.info("ToDo削除: id={}", id);
		if (service.delete(id)) {
			return ResponseEntity.noContent().build(); 
		} else {
			log.warn("削除失敗: id={} が見つかりませんでした", id);
			return ResponseEntity.notFound().build(); 
		}
	}

}
