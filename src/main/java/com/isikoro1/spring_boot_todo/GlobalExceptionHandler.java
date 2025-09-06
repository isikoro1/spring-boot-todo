package com.isikoro1.spring_boot_todo;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	//　カスタムエラーレスポンス用の内部クラス
	static class ErrorResponse {
		private int status;
		private String message;
		
		public ErrorResponse(int status, String message) {
			this.status = status;
			this.message = message;
		}
		
		public int getStatus() { return status; }
		public String getMessage() { return message; }
	}

	// @Valid + @RequestBody のバリデーションエラー
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
		// 最初のエラーメッセージを取得
		String errorMessage = ex.getBindingResult()
								.getAllErrors()
								.get(0)
								.getDefaultMessage();
		
		// カスタムレスポンスを返す
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(400, errorMessage));
	}
	
	// Form系やパラメータバインドでのエラー（念のため追加）
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindErrors(BindException ex) {
		String errorMessage = ex.getBindingResult()
								.getAllErrors()
								.get(0)
								.getDefaultMessage();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new ErrorResponse(400, errorMessage));
	}
	
	
}
