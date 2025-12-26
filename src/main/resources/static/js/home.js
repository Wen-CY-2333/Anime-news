$(document).ready(function () {
    // 搜索框按回车搜索
    $('#search-input').on('keypress', function (e) {
        if (e.which === 13 || e.keyCode === 13) {
            $(this).closest('form').submit();
        }
    });

    // 分享功能 - 复制链接到剪贴板
    $('.text-secondary.text-decoration-none:has(.bi-share)').on('click', function (e) {
        e.preventDefault();

        // 获取新闻链接
        const newsItem = $(this).closest('.mb-4.pb-4.border-bottom');
        const newsLink = newsItem.find('a[target="_blank"]').attr('href');

        // 完整的新闻链接
        const fullNewsUrl = window.location.origin + newsLink;

        // 复制
        const textArea = document.createElement("textarea");
        textArea.value = fullNewsUrl;
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
                alert('复制失败，请手动复制链接: ' + fullNewsUrl);
            }
        } catch (err) {
            alert('复制失败，请手动复制链接: ' + fullNewsUrl);
        }

        document.body.removeChild(textArea);
    });

});

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