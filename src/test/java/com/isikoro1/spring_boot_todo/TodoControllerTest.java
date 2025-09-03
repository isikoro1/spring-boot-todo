package com.isikoro1.spring_boot_todo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.http.MediaType;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(TodoController.class) // コントローラだけをテスト
public class TodoControllerTest {
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TodoRepository todoRepository;
	
	@Test
	void testGetTodoById_Found() throws Exception {
		// モックのTodoを準備
		Todo todo = new Todo();
		todo.setId(1L);
		todo.setTitle("Test Task");
		todo.setDone(false);
		
		given(todoRepository.findById(1L)).willReturn(Optional.of(todo));
		
		// GET /todos/1 を実行 → JSONを検証
		mockMvc.perform(get("/todos/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("Test Task"))
				.andExpect(jsonPath("$.done").value(false));
						
	}
	
	@Test
	void testGetTodoById_NotFound() throws Exception {
		// 存在しないIDの場合は空
		given(todoRepository.findById(999L)).willReturn(Optional.empty());
		
		// GET /todos/999 → 404
		mockMvc.perform(get("/todos/999"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void testCreateTodo() throws Exception {
		// 保存後に返されるTodoをモック
		Todo savedTodo = new Todo();
		savedTodo.setId(1L);
		savedTodo.setTitle("New Task");
		savedTodo.setDone(false);
		
		given(todoRepository.save(any(Todo.class))).willReturn(savedTodo);
		
		// POST /todos 実行
		mockMvc.perform(post("/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"New Task\",\"done\":false}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("New Task"))
				.andExpect(jsonPath("$.done").value(false));
		
	}
	
	
	@Test
	void testUpdateTodo() throws Exception {
		// 更新前
		Todo existingTodo = new Todo();
		existingTodo.setId(1L);
		existingTodo.setTitle("Old Task");
		existingTodo.setDone(false);
		
		// 更新後
		Todo updatedTodo = new Todo();
		updatedTodo.setId(1L);
		updatedTodo.setTitle("Updated Task");
		updatedTodo.setDone(true);
		
		// モック設定: findByIdは existingTodo を返す、saveは updatedTodo を返す
		given(todoRepository.findById(1L)).willReturn(java.util.Optional.of(existingTodo));
		given(todoRepository.save(any(Todo.class))).willReturn(updatedTodo);
		
		// PUT /todos/1
		mockMvc.perform(put("/todos/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Updated Task\",\"done\":true}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("Updated Task"))
				.andExpect(jsonPath("$.done").value(true));
	}
	
	@Test
	void testDeleteTodo_Success() throws Exception {
		// 存在するIDなら true を返す
		given(todoRepository.existsById(1L)).willReturn(true);
		
		// DELETE /todos/1 実行
		mockMvc.perform(delete("/todos/1"))
				.andExpect(status().isNoContent());
		
	}
	
	@Test
	void testDeleteTodo_NotFound() throws Exception {
		// 存在しないIDなら false を返す
		given(todoRepository.existsById(999L)).willReturn(false);
		
		// DELETE /todos/999 実行
		mockMvc.perform(delete("/todos/999"))
				.andExpect(status().isNotFound());
	}
	
	
}
