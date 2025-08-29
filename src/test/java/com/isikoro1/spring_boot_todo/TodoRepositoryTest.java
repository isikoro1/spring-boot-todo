package com.isikoro1.spring_boot_todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {
	
	@Autowired
	private TodoRepository repository;
	
	@Test
	@DisplayName("Todoを保存して、IDで検索できることを確認する")
	void testSaveAndFind() {
		// Todoを１件保存
		Todo todo = new Todo();
		todo.setTitle("JUnit Test");
		todo.setDone(false);
		
		Todo saved = repository.save(todo);
		
		// IDを使って取得
		Todo found = repository.findById(saved.getId()).orElse(null);
		
		// 検証
		assertThat(found).isNotNull();
		assertThat(found.getTitle()).isEqualTo("JUnit Test");
		assertThat(found.isDone()).isFalse();
	}
	
	@Test
	void testUpdateTodo() {
		// 1. Todoを保存
		Todo todo = new Todo();
		todo.setTitle("Bofore Update");
		todo.setDone(false);
		Todo saved = repository.save(todo);
		
		
		// 2. Todoを更新
		saved.setTitle("After Update");
		saved.setDone(true);
		repository.save(saved);
		
		// 3. IDで再取得
		Todo updated = repository.findById(saved.getId()).orElse(null);
		
		// 4. 検証（期待値と比較）
		assertThat(updated).isNotNull();
		assertThat(updated.getTitle()).isEqualTo("After Update");
		assertThat(updated.isDone()).isTrue();
	}
	
	@Test
	void testDeleteTodo() {
		// 1. Todoを保存
		Todo todo = new Todo();
		todo.setTitle("Delete Test");
		todo.setDone(false);
		Todo saved = repository.save(todo);
		
		// 2. 削除する
		repository.deleteById(saved.getId());
		
		// 3. 存在しないことを確認
		boolean exists = repository.findById(saved.getId()).isPresent();
		
		// 4. 検証
		assertThat(exists).isFalse();
	}
	
}


