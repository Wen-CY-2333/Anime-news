// 背景音乐组件模块
(function() {
    // 确保DOM加载完成后执行
    document.addEventListener('DOMContentLoaded', function() {
        // 获取元素
        const music = document.getElementById('bg-music');
        const albumCover = document.getElementById('album-cover');
        
        // 旋转相关变量
        let isPlaying = false;
        let currentAngle = 0;
        let rotationInterval = null;
        const rotationSpeed = 0.5; // 旋转速度，可调整
        let isHovered = false;
        
        // 更新旋转状态
        function updateRotation() {
            // 检查是否有悬停效果
            const scale = isHovered ? 'scale(1.1)' : '';
            // 应用旋转和缩放变换
            albumCover.style.transform = `${scale} rotate(${currentAngle}deg)`;
        }
        
        // 开始旋转
        function startRotation() {
            if (rotationInterval) return;
            
            rotationInterval = setInterval(() => {
                currentAngle += rotationSpeed;
                // 确保角度在0-360之间，避免数值过大
                currentAngle = currentAngle % 360;
                updateRotation();
            }, 30); // 每30ms更新一次，可调整
        }
        
        // 停止旋转
        function stopRotation() {
            if (rotationInterval) {
                clearInterval(rotationInterval);
                rotationInterval = null;
            }
        }
        
        // 处理悬停事件
        albumCover.addEventListener('mouseenter', function() {
            isHovered = true;
            updateRotation();
        });
        
        albumCover.addEventListener('mouseleave', function() {
            isHovered = false;
            updateRotation();
        });

        // 点击事件处理
        albumCover.addEventListener('click', function() {
            if (isPlaying) {
                // 暂停音乐
                music.pause();
                stopRotation();
                isPlaying = false;
            } else {
                // 播放音乐
                music.play();
                startRotation();
                isPlaying = true;
            }
        });
        
        // 页面加载时检查音乐状态（如果音乐是自动播放的情况）
        if (!music.paused) {
            startRotation();
            isPlaying = true;
        }
        
        // 初始化旋转状态
        updateRotation();
    });
})();