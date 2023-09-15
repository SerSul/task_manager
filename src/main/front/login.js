document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("login-form");
    const resultDiv = document.getElementById("result");

    loginForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        // Создаем объект JSON с логином и паролем
        const data = {
            username: username,
            password: password
        };

        // Опции для запроса
        const options = {
            method: "POST",
            body: JSON.stringify(data), // Преобразуем объект в JSON-строку
            headers: {
                "Content-Type": "application/json" // Указываем, что отправляем JSON
            }
        };

        // Отправляем запрос на сервер
        fetch("http://localhost:8080/login", options)
            .then((response) => {
                if (response.ok) {
                    // Если запрос успешен (HTTP-статус 200), перенаправляем пользователя
                    // на страницу успешной аутентификации или другую страницу по вашему выбору
                    window.location.href = "/dashboard";
                } else {
                    // Если запрос вернул ошибку, отобразим сообщение об ошибке
                    resultDiv.innerHTML = "Ошибка аутентификации. Попробуйте еще раз.";
                }
            })
            .catch((error) => {
                console.error("Ошибка при отправке запроса:", error);
            });
    });
});
