package app.auth.security.jwt;

import lombok.Getter;

@Getter
public class JwtValidationResult {
    private boolean valid;       // Флаг, указывающий на то, является ли JWT-токен действительным
    private String errorMessage;  // Сообщение об ошибке, если JWT-токен недействителен

    // Конструктор для создания объекта с флагом действительности
    public JwtValidationResult(boolean valid) {
        this.valid = valid;
    }

    // Конструктор для создания объекта с сообщением об ошибке и флагом "недействительно"
    public JwtValidationResult(String errorMessage) {
        this.valid = false;
        this.errorMessage = errorMessage;
    }

}
