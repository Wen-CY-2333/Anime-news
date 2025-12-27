$(document).ready(function() {
    // 删除点赞
    $('.delete-btn').click(function() {
        var id = $(this).data('id');
        if (confirm('确定要删除这条点赞吗？')) {
            $.ajax({
                url: '/like-manage/delete/' + id,
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
