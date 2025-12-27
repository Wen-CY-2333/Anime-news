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
                console.log('评论提交成功:');
                // 局部更新评论区
                updateCommentSection();
                // 清空评论表单
                $('#comment-content').val('');
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

// 更新评论区（局部刷新）
function updateCommentSection() {
    // 获取当前新闻ID
    const newsId = $('#comment-news-id').val();

    // 发送AJAX请求获取最新评论数据
    $.ajax({
        url: `/news/${newsId}/comments`,
        type: 'GET',
        success: function (response) {
            console.log('获取评论数据成功:', response);
            updateCommentsList(response.comments);
            updateCommentCount(response.commentCount);
        },
        error: function (error) {
            console.error('获取评论数据失败:', error);
        }
    });
}

// 更新评论列表内容
function updateCommentsList(comments) {
    const commentsContainer = $('#comments-list-container');

    // 清空现有评论
    commentsContainer.empty();

    // 渲染评论列表
    comments.forEach(function (comment) {
        // 创建评论元素
        const commentElement = $('<div class="mb-4 pb-4 border-bottom"></div>');
        const commentContent = $('<div class="d-flex align-items-start g-3"></div>');

        // 创建用户头像
        const avatarImg = $('<img>')
            .attr('src', comment.avatar)
            .attr('alt', '用户头像')
            .addClass('img-fluid rounded-circle')
            .css({
                'width': '50px',
                'height': '50px',
                'object-fit': 'cover'
            });

        // 创建右侧内容区域
        const rightContent = $('<div class="flex-grow-1 ms-3"></div>');

        // 创建用户名和时间行
        const headerRow = $('<div class="d-flex justify-content-between align-items-center"></div>');
        const userName = $('<h6 class="mb-1"></h6>').text(comment.userName);
        const createTime = $('<span class="text-xs text-muted"></span>').text(formatDate(comment.createTime));

        headerRow.append(userName, createTime);

        // 创建评论内容
        const commentText = $('<p class="text-muted small"></p>').text(comment.commentContent);

        // 组装元素
        rightContent.append(headerRow, commentText);
        commentContent.append(avatarImg, rightContent);
        commentElement.append(commentContent);

        // 添加到容器
        commentsContainer.append(commentElement);
    });
}

// 更新评论数量显示
function updateCommentCount(commentCount) {
    $('.comment-count').text(commentCount);
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

// 更新点赞状态（局部刷新）
function updateLikeStatus(response) {

    // 更新隐藏域的值
    $('#is-liked').val(response.isLiked);

    // 更新点赞数量
    $('.like-count').text(response.likeCount);
    console.log('点赞数量已更新为:', response.likeCount);

    // 更新点赞按钮样式
    updateLikeButton();
    console.log('点赞按钮状态已更新');
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
            success: function (response) {
                console.log('取消点赞成功:', response);
                updateLikeStatus(response);
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
            success: function (response) {
                console.log('点赞成功:', response);
                updateLikeStatus(response);
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

// 格式化日期显示
function formatDate(dateString) {
    if (!dateString) return '';

    const date = new Date(dateString);
    if (isNaN(date.getTime())) return '';

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}