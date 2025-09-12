package com.isikoro1.spring_boot_todo;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class) // Mockitoを使うための設定
public class TodoServiceTest {

	@Mock
	private TodoRepository repository; // DB アクセスをモック化
	
	@InjectMocks
	private TodoService service; // repositoryを注入してテスト対象にする
	
	@Test
	void testFindAll() {
		// モックの戻り値を準備
		Todo todo1 = new Todo();
		todo1.setId(1L);
		todo1.setTitle("Task1");
		todo1.setDone(false);
		Todo todo2 = new Todo();
		todo2.setId(2L);
		todo2.setTitle("Task2");
		todo2.setDone(true);
		
		given(repository.findAll()).willReturn(Arrays.asList(todo1, todo2));
		
		// 実行
		List<Todo> result = service.findAll();
		
		// 検証
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getTitle()).isEqualTo("Task1");
		assertThat(result.get(1).isDone()).isTrue();		
	}
	
	@Test
    void testFindById_Found() {
        Todo todo = new Todo(); todo.setId(1L); todo.setTitle("Test"); todo.setDone(false);
        given(repository.findById(1L)).willReturn(Optional.of(todo));

        Optional<Todo> result = service.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test");
    }
	
	@Test
    void testFindById_NotFound() {
        given(repository.findById(999L)).willReturn(Optional.empty());

        Optional<Todo> result = service.findById(999L);

        assertThat(result).isNotPresent();
    }

	@Test
    void testCreate() {
        Todo todo = new Todo(); todo.setTitle("New Task"); todo.setDone(false);
        Todo saved = new Todo(); saved.setId(1L); saved.setTitle("New Task"); saved.setDone(false);
        given(repository.save(any(Todo.class))).willReturn(saved);

        Todo result = service.create(todo);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New Task");
    }
	
	@Test
    void testUpdate() {
        Todo updated = new Todo(); updated.setId(1L); updated.setTitle("Updated"); updated.setDone(true);
        given(repository.save(any(Todo.class))).willReturn(updated);

        Todo result = service.update(1L, updated);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Updated");
    }
	
	@Test
    void testDelete_Success() {
        given(repository.existsById(1L)).willReturn(true);

        boolean result = service.delete(1L);

        assertThat(result).isTrue();
        verify(repository).deleteById(1L); // 削除が呼ばれたか確認
    }
	
	 @Test
	    void testDelete_NotFound() {
	        given(repository.existsById(999L)).willReturn(false);

	        boolean result = service.delete(999L);

	        assertThat(result).isFalse();
	    }
	
}
