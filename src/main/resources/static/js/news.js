$(document).ready(function () {

    // 更新点赞按钮状态
    updateLikeButton();

    // 绑定点赞按钮点击事件
    $('.like-btn').on('click', function () {
        toggleLike($(this));
    });

    // 绑定评论表单提交事件
    $('#comment-form').on('submit', function (e) {
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
            success: function () {
                // 提交成功后刷新页面
                location.reload();
            },
            error: function (error) {
                // 处理错误
                console.error('评论提交失败:', error);
                alert('评论提交失败，请稍后重试');
            }
        });
    });

    // 页面加载时如果有 #comment-section 就平滑滚动
    if (window.location.hash === '#comment-section') {
        scrollToCommentSection();
    }

});

// 平滑滚动到评论部分
function scrollToCommentSection() {
    const commentSection = document.getElementById('comment-section');
    if (commentSection) {
        commentSection.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });
    }
}

// 更新点赞按钮状态
function updateLikeButton() {
    const isLiked = $('#is-liked').val() === 'true';
    const likeBtn = $('.like-btn');

    if (isLiked) {
        likeBtn.removeClass('btn-outline-secondary').addClass('btn-primary');
        likeBtn.find('i').removeClass('bi-heart').addClass('bi-heart-fill');
    } else {
        likeBtn.removeClass('btn-primary').addClass('btn-outline-secondary');
        likeBtn.find('i').removeClass('bi-heart-fill').addClass('bi-heart');
    }
}

// 切换点赞状态
function toggleLike(btn) {
    const newsId = btn.data('news-id');
    const userId = $('#current-user-id').val();
    const isLikedValue = $('#is-liked').val();

    // 如果已经点赞，执行取消点赞
    if (isLikedValue === 'true') {

        // 转换ID为数字类型
        const convertedUserId = parseInt(userId);  // 转换用户ID
        const convertedNewsId = parseInt(newsId);  // 转换新闻ID

        $.ajax({
            url: '/unlike',
            type: 'POST',
            data: {
                userId: convertedUserId,  // 使用转换后的值
                newsId: convertedNewsId   // 使用转换后的值
            },
            success: function () {
                location.reload();
            },
            error: function (error) {
                console.error('取消点赞失败:', error);
                alert('取消点赞失败，请重试');
            }
        });
    } else {

        // 转换ID为数字类型
        const convertedUserId = parseInt(userId);  // 转换用户ID
        const convertedNewsId = parseInt(newsId);  // 转换新闻ID

        // 发送点赞请求
        $.ajax({
            url: '/like',
            type: 'POST',
            data: {
                userId: convertedUserId,  // 使用转换后的值
                newsId: convertedNewsId   // 使用转换后的值
            },
            success: function () {
                location.reload();
            },
            error: function (error) {
                console.error('点赞失败:', error);
                alert('点赞失败，请重试');
            }
        });
    }
}