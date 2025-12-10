const loginCard = document.getElementById('login-card');
const registerCard = document.getElementById('register-card');
const toRegister = document.getElementById('to-register');
const toLogin = document.getElementById('to-login');

toRegister.addEventListener('click', function () {
    loginCard.style.display = 'none';
    registerCard.style.display = 'block';
});

toLogin.addEventListener('click', function () {
    registerCard.style.display = 'none';
    loginCard.style.display = 'block';
});