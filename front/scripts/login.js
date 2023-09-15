document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("login-form");
    const resultDiv = document.getElementById("result");

    loginForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        const data = {
            username: username,
            password: password
        };

        // Опции для запроса
        const options = {
            method: "POST",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
            }
        };

        // Отправляем запрос на сервер
        fetch("http://localhost:8080/login", options)
            .then((response) => {
                if (response.ok) {

                    window.location.href = "/dashboard";
                } else {

                    resultDiv.innerHTML = "Ошибка аутентификации. Попробуйте еще раз.";
                }
            })
            .catch((error) => {
                console.error("Ошибка при отправке запроса:", error);
            });
    });
});
