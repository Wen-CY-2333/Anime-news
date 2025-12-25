$(document).ready(function() {
    // 添加新闻按钮点击事件
    $('#saveAddNewsBtn').click(function() {
        // 获取表单数据
        var formData = {
            title: $('#addNewsTitle').val(),
            tag: $('#addNewsTag').val(),
            url: $('#addNewsUrl').val(),
            time: $('#addNewsTime').val(),
            note: $('#addNewsNote').val(),
            content: $('#addNewsContent').val(),
            image: $('#addNewsImage').val()
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/news/add',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#addNewsModal').modal('hide');
                // 清空表单
                $('#addNewsForm')[0].reset();
                // 刷新页面
                location.reload();
            },
            error: function(error) {
                console.error('添加新闻失败:', error);
                alert('添加失败！');
            }
        });
    });

    // 编辑新闻按钮点击事件
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
                $('#editNewsTag').val(news.tag);
                $('#editNewsUrl').val(news.url);
                $('#editNewsTime').val(news.time);
                $('#editNewsNote').val(news.note);
                $('#editNewsContent').val(news.content);
                $('#editNewsImage').val(news.image);
            },
            error: function(error) {
                console.error('获取新闻信息失败:', error);
                alert('获取新闻信息失败！');
            }
        });
    });

    // 保存编辑新闻按钮点击事件
    $('#saveEditBtn').click(function() {
        // 获取表单数据
        var formData = {
            id: $('#editNewsId').val(),
            title: $('#editNewsTitle').val(),
            tag: $('#editNewsTag').val(),
            url: $('#editNewsUrl').val(),
            time: $('#editNewsTime').val(),
            note: $('#editNewsNote').val(),
            content: $('#editNewsContent').val(),
            image: $('#editNewsImage').val()
        };

        $.ajax({
            type: 'POST',
            url: '/news/edit',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#editNewsModal').modal('hide');
                // 刷新页面
                location.reload();
            },
            error: function(error) {
                console.error('修改新闻失败:', error);
                alert('修改失败！');
            }
        });
    });

    // 删除新闻按钮点击事件
    $('.delete-btn').click(function() {
        var id = $(this).data('id');
        var row = $(this).closest('tr');
        
        // 确认删除
        if (confirm('确定要删除这条新闻吗？')) {
            $.ajax({
                type: 'POST',
                url: '/news/delete/' + id,
                success: function() {
                    location.reload();
                },
                error: function(error) {
                    console.error('删除新闻失败:', error);
                    alert('删除失败！');
                }
            });
        }
    });
});