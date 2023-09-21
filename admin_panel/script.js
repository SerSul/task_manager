let accessToken;

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
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            const responseDiv = document.getElementById('response');
            responseDiv.innerHTML = JSON.stringify(data, null, 2);

            if (data.accessToken) {

                const accessToken = data.accessToken;

                localStorage.setItem('accessToken', accessToken);

                // Перенаправляем пользователя на index.html
                window.location.href = 'index.html';
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
});











const userList = document.getElementById("user-list");

function displayUsers() {
    userList.innerHTML = "";
    users.forEach(user => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td><button onclick="deleteUser(${user.id})">Удалить</button></td>
        `;
        userList.appendChild(row);
    });
}


function deleteUser(userId) {
    const index = users.findIndex(user => user.id === userId);
    if (index !== -1) {
        users.splice(index, 1);
        displayUsers();
    }
}

// Инициализация при загрузке страницы
displayUsers();
