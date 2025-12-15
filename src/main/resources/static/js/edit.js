$(document).ready(function () {

    $('form').submit(function (e) {
        e.preventDefault(); // 阻止默认提交行为

        //获取当前时间
        var date = new Date();
        var year = date.getFullYear();
        var month = String(date.getMonth() + 1).padStart(2, '0');
        var day = String(date.getDate()).padStart(2, '0');
        var currentTime = year + '-' + month + '-' + day;

        // 获取表单数据
        var formData = {
            id: $('#id').val(),
            url: $('#url').val(),
            title: $('#title').val(),
            image: $('#image').val(),
            tag: $('#tag').val(),
            content: $('#content').val(),
            time: currentTime
        };
        
        // 发送AJAX请求
        $.ajax({
            type: 'POST',
            url: '/edit/model',
            contentType: 'application/x-www-form-urlencoded',
            data: formData,
            success: function (response) {
                setTimeout(function () {
                    alert('修改成功');
                    // 修改成功后跳转到列表页
                    window.location.href = '/list';
                }, 100);
            },
            error: function (error) {
                console.error('Error editing news:', error);
            }
        });

    });

});