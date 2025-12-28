$(document).ready(function() {
    // 删除评论
    $('.delete-btn').click(function() {
        var id = $(this).data('id');
        if (confirm('确定要删除这条评论吗？')) {
            $.ajax({
                url: '/comment-manage/delete/' + id,
                type: 'POST',
                success: function() {
                    window.location.reload();
                },
                error: function() {
                    alert('删除失败！');
                }
            });
        }
    });
});
