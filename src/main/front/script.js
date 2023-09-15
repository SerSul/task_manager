document.addEventListener("DOMContentLoaded", function() {
    const sendRequestButton = document.getElementById("sendRequest");
    const responseDiv = document.getElementById("response");

    sendRequestButton.addEventListener("click", function() {
        // Отправка AJAX-запроса
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "https://localhost:8080/registration", true); // Замените "/api/your-endpoint" на ваш URL-адрес

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const response = xhr.responseText;
                console.log("Ответ:", response);
                responseDiv.textContent = "Ответ: " + response;
            }
        };

        xhr.send();
    });
});
