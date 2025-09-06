package com.isikoro1.spring_boot_todo;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
	
	private final TodoRepository repository;
	
	public TodoService(TodoRepository repository) {
		this.repository = repository;
	}
	
	public List<Todo> findAll() {
		return repository.findAll();
	}
	
	
	public Optional<Todo> findById(Long id) {
		return repository.findById(id);
	}
	
	public Todo create(Todo todo) {
		return repository.save(todo);
	}
	
	public Todo update(Long id, Todo updated) {
		updated.setId(id);
		return repository.save(updated);
	}
	
	public boolean delete(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
	
}
