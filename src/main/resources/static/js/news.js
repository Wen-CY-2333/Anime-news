$(document).ready(function() {
    // 修改按钮
    $('.edit-btn').click(function() {
        var id = $(this).data('id');
        // 发送请求获取新闻信息
        $.ajax({
            type: 'GET',
            url: '/news/find/' + id,
            success: function(news) {
                // 填充编辑表单
                $('#editNewsId').val(news.id);
                $('#editNewsTitle').val(news.title);
                $('#editNewsNote').val(news.note);
                $('#editNewsContent').val(news.content);
                $('#editNewsUrl').val(news.url);
                $('#editNewsImage').val(news.image);
                $('#editNewsTag').val(news.tag);
                $('#editNewsTime').val(news.time);
            },
            error: function(error) {
                console.error('获取新闻信息失败:', error);
            }
        });
    });

    // 修改模态框的保存按钮
    $('#saveEditBtn').click(function() {
        // 获取表单数据
        var formData = {
            id: $('#editNewsId').val(),
            title: $('#editNewsTitle').val(),
            note: $('#editNewsNote').val(),
            content: $('#editNewsContent').val(),
            url: $('#editNewsUrl').val(),
            image: $('#editNewsImage').val(),
            tag: $('#editNewsTag').val(),
            time: $('#editNewsTime').val()
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/news/edit',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#editNewsModal').modal('hide');
                // 刷新页面
                window.location.reload();
            },
            error: function(error) {
                console.error('编辑新闻失败:', error);
            }
        });
    });

    // 删除按钮
    $('.delete-btn').click(function() {
        var id = $(this).data('id');
        if (confirm('确定要删除这条新闻吗？')) {
            // 发送DELETE请求
            $.ajax({
                type: 'POST',
                url: '/news/delete/' + id,
                success: function() {
                    // 刷新页面
                    window.location.reload();
                },
                error: function(error) {
                    console.error('删除新闻失败:', error);
                }
            });
        }
    });

    // 发布新闻按钮
    $('#saveAddNewsBtn').click(function() {
        // 获取表单数据
        var formData = {
            title: $('#addNewsTitle').val(),
            note: $('#addNewsNote').val(),
            content: $('#addNewsContent').val(),
            url: $('#addNewsUrl').val(),
            image: $('#addNewsImage').val(),
            tag: $('#addNewsTag').val(),
            time: $('#addNewsTime').val()
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/news/add',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#addNewsModal').modal('hide');
                // 刷新页面
                window.location.reload();
            },
            error: function(error) {
                console.error('发布新闻失败:', error);
            }
        });
    });
});
