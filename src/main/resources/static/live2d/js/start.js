// 模型列表
var models = [
    "/live2d/model/hallo/model.json",
    "/live2d/model/date/model.json",
    "/live2d/model/wed_16/wed_16.model.json",
    "/live2d/model/bilibili-22/index.json",
    "/live2d/model/bilibili-33/index.json",
    "/live2d/model/cat-black/model.json",
    "/live2d/model/cat-white/model.json",
    "/live2d/model/rem_2/model.json",
    "/live2d/model/HK416-1-normal/model.json",
    "/live2d/model/HK416-2-destroy/model.json",
    "/live2d/model/HK416-2-normal/model.json",
    "/live2d/model/kobayaxi/model.json",
    "/live2d/model/platelet/model.json",
    "/live2d/model/terisa/model.json",
    "/live2d/model/kp31/model.json"
];

// 初始加载模型
var currentModelIndex = 8;
loadlive2d("live2d", models[currentModelIndex]);

// 切换看板娘
$('.switch-button').click(function () {
    currentModelIndex = (currentModelIndex + 1) % models.length;
    loadlive2d("live2d", models[currentModelIndex]);
    console.log(currentModelIndex, models[currentModelIndex]);
});

// 实现看板娘的拖拽功能
$('#landlord').draggable({
    cursor: 'move',
    containment: false, // 允许拖动超出父容器边界

    // 拖动开始,暂停动画和过渡效果
    start: function (event, ui) {
        $(this).find('.message').css('animation-play-state', 'paused');
        $(this).css('transition', 'none');
    },
    // 拖动结束,恢复动画和过渡效果
    stop: function (event, ui) {
        $(this).find('.message').css('animation-play-state', 'running');
        $(this).css('transition', 'all .3s ease-in-out');
    }
});