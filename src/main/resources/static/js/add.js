$(document).ready(function () {

    $('form').submit(function (e) {
        e.preventDefault(); // 阻止默认提交行为

        //获取当前时间
        var currentTime = new Date().getTime();

        // 获取表单数据
        var formData = {
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
            url: '/add/model',
            contentType: 'application/x-www-form-urlencoded',
            data: formData,
            success: function (response) {
                // 清空表单
                $('form')[0].reset();
                setTimeout(function () {
                    alert('添加成功');
                }, 100);// 添加延时，添加并刷新后显示成功提示
            },
            error: function (error) {
                console.error('Error adding news:', error);
            }
        });

    });

});