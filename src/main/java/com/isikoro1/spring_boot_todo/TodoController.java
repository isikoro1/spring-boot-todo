package com.isikoro1.spring_boot_todo;

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
	public void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
