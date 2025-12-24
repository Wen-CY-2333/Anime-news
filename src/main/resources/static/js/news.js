$(document).ready(function() {
    $('#comment-form').on('submit', function(e) {
        // 阻止表单默认提交行为
        e.preventDefault();
        
        // 获取表单数据
        const newsId = $('#comment-news-id').val();
        const commentContent = $('#comment-content').val().trim();
        
        // 简单验证
        if (!commentContent) {
            alert('评论内容不能为空');
            return;
        }
        
        // 发送ajax请求
        $.ajax({
            url: '/news/comment',
            type: 'POST',
            data: {
                newsId: newsId,
                commentContent: commentContent
            },
            success: function() {
                // 提交成功后刷新页面
                location.reload();
            },
            error: function(error) {
                // 处理错误
                console.error('评论提交失败:', error);
                alert('评论提交失败，请稍后重试');
            }
        });
    });
});