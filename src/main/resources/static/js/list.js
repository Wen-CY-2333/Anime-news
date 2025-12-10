$(document).ready(function () {

    $('form').submit(function (e) {
        e.preventDefault(); // 阻止默认提交行为

        // 获取表单数据
        var formData = {
            url: $('#url').val(),
            title: $('#title').val(),
            image: $('#image').val()
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
                // 刷新表格
                refreshNewsTable();
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

// 刷新表格
function refreshNewsTable() {
    $.ajax({
        type: 'GET',
        url: '/list',
        success: function (response) {
            // 从响应中提取表格内容
            var tableHtml = $(response).find('#news-table-container').html();
            // 更新当前页面的表格
            $('#news-table-container').html(tableHtml);
        },
        error: function (error) {
            console.error('Error refreshing table:', error);
        }
    });
}

// 删除新闻
function deleteNews(id) {
    $.post('/delete/' + id, function () {
        // 删除成功后刷新表格
        refreshNewsTable();
        setTimeout(function () {
            alert('删除成功');
        }, 100);// 添加延时，删除并刷新后显示成功提示
    });
}