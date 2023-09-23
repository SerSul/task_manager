
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const data = {
        username: username,
        password: password
    };

    fetch('http://localhost:8080/api/auth/signin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data),

    })
        .then(response => response.json()) // Преобразование ответа в JSON
        .then(data => {
            // Извлечение access token из JSON-ответа
            const accessToken = data.accessToken;

            // Вывод токена в консоль
            console.log('Access Token:', accessToken);

            // Сохранение токена в localStorage
            localStorage.setItem('accessToken', accessToken);

            // Переход на страницу index.html
            if (accessToken !== undefined) {
                window.location.href = 'index.html';
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
});
