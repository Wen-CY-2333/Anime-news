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

window.addEventListener('load', function() {
    // 检查是否有注册错误，如果有则显示注册表单
    const registerError = document.querySelector('.register-error');
    if (registerError && registerError.textContent.trim() !== '') {
        loginCard.style.display = 'none';
        registerCard.style.display = 'block';
    }
    // 检查是否有登录错误，如果有则显示登录表单
    const loginError = document.querySelector('.login-error');
    if (loginError && loginError.textContent.trim() !== '') {
        registerCard.style.display = 'none';
        loginCard.style.display = 'block';
    }
});