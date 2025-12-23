$(document).ready(function() {
    // 添加音乐按钮点击事件
    $('#saveAddMusicBtn').click(function() {
        // 获取表单数据
        var formData = {
            bvid: $('#addMusicBvid').val(),
            title: $('#addMusicTitle').val(),
            pic: $('#addMusicPic').val(),
            url: $('#addMusicUrl').val(),
            author: $('#addMusicAuthor').val(),
            play: $('#addMusicPlay').val(),
            review: $('#addMusicReview').val(),
            favorites: $('#addMusicFavorites').val(),
            cateId: $('#addMusicCateId').val(),
            cateName: $('#addMusicCateName').val()
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/music-manage/add',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#addMusicModal').modal('hide');
                // 刷新页面
                location.reload();
            },
            error: function(error) {
                console.error('添加音乐失败:', error);
                alert('添加失败！');
            }
        });
    });

    // 编辑音乐按钮点击事件
    $('.edit-music-btn').click(function() {
        var id = $(this).data('id');
        // 发送请求获取音乐信息
        $.ajax({
            type: 'GET',
            url: '/music-manage/find/' + id,
            success: function(music) {
                // 填充编辑表单
                $('#editMusicId').val(music.id);
                $('#editMusicBvid').val(music.bvid);
                $('#editMusicTitle').val(music.title);
                $('#editMusicPic').val(music.pic);
                $('#editMusicUrl').val(music.url);
                $('#editMusicAuthor').val(music.author);
                $('#editMusicPlay').val(music.play);
                $('#editMusicReview').val(music.review);
                $('#editMusicFavorites').val(music.favorites);
                $('#editMusicCateId').val(music.cateId);
                $('#editMusicCateName').val(music.cateName);
            },
            error: function(error) {
                console.error('获取音乐信息失败:', error);
                alert('获取音乐信息失败！');
            }
        });
    });

    // 保存编辑按钮点击事件
    $('#saveEditMusicBtn').click(function() {
        // 获取表单数据
        var formData = {
            id: $('#editMusicId').val(),
            bvid: $('#editMusicBvid').val(),
            title: $('#editMusicTitle').val(),
            pic: $('#editMusicPic').val(),
            url: $('#editMusicUrl').val(),
            author: $('#editMusicAuthor').val(),
            play: $('#editMusicPlay').val(),
            review: $('#editMusicReview').val(),
            favorites: $('#editMusicFavorites').val(),
            cateId: $('#editMusicCateId').val(),
            cateName: $('#editMusicCateName').val()
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/music-manage/edit',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#editMusicModal').modal('hide');
                // 刷新页面
                location.reload();
            },
            error: function(error) {
                console.error('编辑音乐失败:', error);
                alert('修改失败！');
            }
        });
    });

    // 删除音乐按钮点击事件
    $('.delete-music-btn').click(function() {
        var id = $(this).data('id');
        if (confirm('确定要删除这个音乐吗？')) {
            // 发送POST请求
            $.ajax({
                type: 'POST',
                url: '/music-manage/delete/' + id,
                success: function() {
                    // 刷新页面
                    location.reload();
                },
                error: function(error) {
                    console.error('删除音乐失败:', error);
                    alert('删除失败！');
                }
            });
        }
    });
});