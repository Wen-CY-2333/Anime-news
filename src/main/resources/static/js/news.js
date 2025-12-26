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

    // 分享功能 - 复制链接到剪贴板
    $('.btn.btn-sm.btn-outline-secondary.glass:has(.bi-share)').on('click', function (e) {
        e.preventDefault();

        // 获取新闻链接
        const newsUrl = window.location.href;


        // 复制
        const textArea = document.createElement("textarea");
        textArea.value = newsUrl;
        textArea.style.position = "fixed";
        textArea.style.left = "-999999px";
        textArea.style.top = "-999999px";
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();

        try {
            const successful = document.execCommand('copy');
            if (successful) {
                showCopyNotification();
            } else {
                alert('复制失败，请手动复制链接: ' + newsUrl);
            }
        } catch (err) {
            alert('复制失败，请手动复制链接: ' + newsUrl);
        }

        document.body.removeChild(textArea);
    });
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

        $.ajax({
            url: '/unlike',
            type: 'POST',
            data: {
                userId: userId,
                newsId: newsId
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

        // 发送点赞请求
        $.ajax({
            url: '/like',
            type: 'POST',
            data: {
                userId: userId,
                newsId: newsId
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

// 显示复制成功通知
function showCopyNotification() {
    // 创建黑色蒙版背景，初始透明度为0，添加过渡效果
    const overlay = $('<div class="notification-overlay" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.5); z-index: 9998; display: flex; justify-content: center; align-items: center; opacity: 0; transition: opacity 0.3s ease-out;"></div>');

    // 创建一个临时的通知元素，居中显示，初始缩放为0.8，透明度为0，添加过渡效果
    const notification = $('<div class="copy-notification" style="background: #39C5BB; color: white; padding: 20px 40px; border-radius: 8px; z-index: 9999; box-shadow: 0 4px 12px rgba(0,0,0,0.3); font-size: 16px; text-align: center; transform: scale(0.8); opacity: 0; transition: all 0.3s ease-out;">链接已复制到剪贴板！</div>');

    // 添加到页面
    $('body').append(overlay);
    overlay.append(notification);

    // 触发重排，确保过渡效果能正常执行
    overlay[0].offsetHeight;
    notification[0].offsetHeight;

    // 缓入效果：设置最终状态，由CSS过渡实现动画
    overlay.css('opacity', '1');
    notification.css({
        'transform': 'scale(1)',
        'opacity': '1'
    });

    setTimeout(() => {
        // 缓出效果：设置初始状态，由CSS过渡实现动画
        notification.css({
            'transform': 'scale(0.8)',
            'opacity': '0',
            'transition': 'all 0.3s ease-in'
        });

        // 等待通知动画完成后，再执行背景淡出
        setTimeout(() => {
            overlay.css({
                'opacity': '0',
                'transition': 'opacity 0.2s ease-in'
            });

            // 等待背景动画完成后，移除元素
            setTimeout(() => {
                overlay.remove();
            }, 200);
        }, 300);
    }, 2000);
}