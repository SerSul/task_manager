const accessToken = localStorage.getItem('accessToken');
document.addEventListener('DOMContentLoaded', function () {

    function loadUsers() {
        fetch('http://localhost:8080/api/admin/getallusers', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + accessToken, // Замените yourAccessToken на актуальный токен
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                const userList = document.getElementById('user-list');
                userList.innerHTML = ''; // Очистить текущий список пользователей

                data.forEach(user => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>Действия</td>
                    `;

                    userList.appendChild(row);
                });
            })
            .catch(error => {
                console.error('Ошибка при загрузке пользователей:', error);
            });
    }

    // Вызываем функцию loadUsers при загрузке страницы
    loadUsers();
});

console.log(accessToken);
