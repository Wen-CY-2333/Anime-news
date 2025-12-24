$(document).ready(function() {
    // 添加番剧按钮点击事件
    $('#saveAddAnimeBtn').click(function() {
        // 获取表单数据
        var formData = {
            seasonId: $('#addAnimeSeasonId').val(),
            title: $('#addAnimeTitle').val(),
            cover: $('#addAnimeCover').val(),
            url: $('#addAnimeUrl').val(),
            pubIndex: $('#addAnimePubIndex').val(),
            week: $('#addAnimeWeek').val(),
            date: $('#addAnimeDate').val(),
            pubTime: $('#addAnimePubTime').val(),
            isToday: $('#addAnimeIsToday').prop('checked') ? 1 : 0
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/anime-manage/add',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#addAnimeModal').modal('hide');
                // 刷新页面
                location.reload();
            },
            error: function(error) {
                console.error('添加番剧失败:', error);
                alert('添加失败！');
            }
        });
    });

    // 编辑番剧按钮点击事件
    $('.edit-anime-btn').click(function() {
        var id = $(this).data('id');
        // 发送请求获取番剧信息
        $.ajax({
            type: 'GET',
            url: '/anime-manage/find/' + id,
            success: function(anime) {
                // 填充编辑表单
                $('#editAnimeId').val(anime.id);
                $('#editAnimeSeasonId').val(anime.seasonId);
                $('#editAnimeTitle').val(anime.title);
                $('#editAnimeCover').val(anime.cover);
                $('#editAnimeUrl').val(anime.url);
                $('#editAnimePubIndex').val(anime.pubIndex);
                $('#editAnimeWeek').val(anime.week);
                $('#editAnimeDate').val(anime.date);
                $('#editAnimePubTime').val(anime.pubTime);
                $('#editAnimeIsToday').prop('checked', anime.isToday == 1);
            },
            error: function(error) {
                console.error('获取番剧信息失败:', error);
                alert('获取番剧信息失败！');
            }
        });
    });

    // 保存编辑按钮点击事件
    $('#saveEditAnimeBtn').click(function() {
        // 获取表单数据
        var formData = {
            id: $('#editAnimeId').val(),
            seasonId: $('#editAnimeSeasonId').val(),
            title: $('#editAnimeTitle').val(),
            cover: $('#editAnimeCover').val(),
            url: $('#editAnimeUrl').val(),
            pubIndex: $('#editAnimePubIndex').val(),
            week: $('#editAnimeWeek').val(),
            date: $('#editAnimeDate').val(),
            pubTime: $('#editAnimePubTime').val(),
            isToday: $('#editAnimeIsToday').prop('checked') ? 1 : 0
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/anime-manage/edit',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#editAnimeModal').modal('hide');
                // 刷新页面
                location.reload();
            },
            error: function(error) {
                console.error('编辑番剧失败:', error);
                alert('修改失败！');
            }
        });
    });

    // 删除番剧按钮点击事件
    $('.delete-anime-btn').click(function() {
        var id = $(this).data('id');
        if (confirm('确定要删除这个番剧吗？')) {
            // 发送POST请求
            $.ajax({
                type: 'POST',
                url: '/anime-manage/delete/' + id,
                success: function() {
                    // 刷新页面
                    location.reload();
                },
                error: function(error) {
                    console.error('删除番剧失败:', error);
                    alert('删除失败！');
                }
            });
        }
    });
});