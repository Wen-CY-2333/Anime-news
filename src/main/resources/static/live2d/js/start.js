// 模型列表
var models = [
    "/live2d/model/miku/miku.model.json",
    "/live2d/model/snow_miku/model.json",
    "/live2d/model/rem/model.json",
    "/live2d/model/22/model.default.json",
    "/live2d/model/33/model.default.json"
];

// 角色换装配置
var characterOutfits = {
    "22": [
        "/live2d/model/22/model.default.json",
        "/live2d/model/22/model.2016.xmas.1.json",
        "/live2d/model/22/model.2016.xmas.2.json",
        "/live2d/model/22/model.2017.newyear.json",
        "/live2d/model/22/model.2017.cba-normal.json",
        "/live2d/model/22/model.2017.cba-super.json",
        "/live2d/model/22/model.2017.school.json",
        "/live2d/model/22/model.2017.summer.normal.1.json",
        "/live2d/model/22/model.2017.summer.normal.2.json",
        "/live2d/model/22/model.2017.summer.super.1.json",
        "/live2d/model/22/model.2017.summer.super.2.json",
        "/live2d/model/22/model.2017.tomo-bukatsu.high.json",
        "/live2d/model/22/model.2017.tomo-bukatsu.low.json",
        "/live2d/model/22/model.2017.valley.json",
        "/live2d/model/22/model.2017.vdays.json",
        "/live2d/model/22/model.2018.bls-summer.json",
        "/live2d/model/22/model.2018.bls-winter.json",
        "/live2d/model/22/model.2018.lover.json",
        "/live2d/model/22/model.2018.spring.json"
    ],
    "33": [
        "/live2d/model/33/model.default.json",
        "/live2d/model/33/model.2016.xmas.1.json",
        "/live2d/model/33/model.2016.xmas.2.json",
        "/live2d/model/33/model.2017.newyear.json",
        "/live2d/model/33/model.2017.cba-normal.json",
        "/live2d/model/33/model.2017.cba-super.json",
        "/live2d/model/33/model.2017.school.json",
        "/live2d/model/33/model.2017.summer.normal.1.json",
        "/live2d/model/33/model.2017.summer.normal.2.json",
        "/live2d/model/33/model.2017.summer.super.1.json",
        "/live2d/model/33/model.2017.summer.super.2.json",
        "/live2d/model/33/model.2017.tomo-bukatsu.high.json",
        "/live2d/model/33/model.2017.tomo-bukatsu.low.json",
        "/live2d/model/33/model.2017.valley.json",
        "/live2d/model/33/model.2017.vdays.json",
        "/live2d/model/33/model.2018.bls-summer.json",
        "/live2d/model/33/model.2018.bls-winter.json",
        "/live2d/model/33/model.2018.lover.json",
        "/live2d/model/33/model.2018.spring.json"
    ]
};

// 记录当前角色的换装状态
var currentOutfitIndex = {
    "22": 0,
    "33": 0
};

// 记录当前模型索引
var currentModelIndex = 0;

// 缓存管理常量
const STORAGE_KEYS = {
    CURRENT_MODEL_INDEX: 'live2d_current_model_index',
    OUTFIT_INDICES: 'live2d_outfit_indices'
};

// 初始化或恢复状态
function initializeState() {
    // 尝试从localStorage恢复状态
    const savedModelIndex = localStorage.getItem(STORAGE_KEYS.CURRENT_MODEL_INDEX);
    const savedOutfitIndices = localStorage.getItem(STORAGE_KEYS.OUTFIT_INDICES);
    
    if (savedModelIndex !== null) {
        currentModelIndex = parseInt(savedModelIndex);
    }
    
    if (savedOutfitIndices) {
        try {
            const outfitIndices = JSON.parse(savedOutfitIndices);
            currentOutfitIndex = { ...currentOutfitIndex, ...outfitIndices };
        } catch (e) {
            console.warn('Failed to parse saved outfit indices:', e);
        }
    }
    
    // 确保索引在有效范围内
    currentModelIndex = Math.max(0, Math.min(currentModelIndex, models.length - 1));
}

// 保存状态到localStorage
function saveState() {
    try {
        localStorage.setItem(STORAGE_KEYS.CURRENT_MODEL_INDEX, currentModelIndex.toString());
        localStorage.setItem(STORAGE_KEYS.OUTFIT_INDICES, JSON.stringify(currentOutfitIndex));
    } catch (e) {
        console.warn('Failed to save Live2D state:', e);
    }
}

// 清除缓存状态
function clearCache() {
    try {
        localStorage.removeItem(STORAGE_KEYS.CURRENT_MODEL_INDEX);
        localStorage.removeItem(STORAGE_KEYS.OUTFIT_INDICES);
        console.log('Live2D cache cleared');
    } catch (e) {
        console.warn('Failed to clear Live2D cache:', e);
    }
}

// 初始加载模型（带缓存检查）
initializeState();
loadlive2d("live2d", models[currentModelIndex]);

// 切换看板娘
$('.switch-button').click(function () {
    currentModelIndex = (currentModelIndex + 1) % models.length;
    loadlive2d("live2d", models[currentModelIndex]);
    console.log(currentModelIndex, models[currentModelIndex]);
    saveState(); // 保存状态
});

// 获取当前角色名称
function getCurrentCharacter() {
    var currentModel = models[currentModelIndex];
    if (currentModel.includes("/22/")) {
        return "22";
    } else if (currentModel.includes("/33/")) {
        return "33";
    }
    return null;
}

// 绑定换装按钮点击事件，先解绑所有现有的事件处理器，避免重复绑定
$('.outfit-button').off('click').click(function () {
    var currentCharacter = getCurrentCharacter();
    if (currentCharacter) {
        switchCharacterOutfit(currentCharacter);
    }
});

// 角色换装功能
function switchCharacterOutfit(characterName) {
    if (!characterOutfits[characterName]) return;

    var outfits = characterOutfits[characterName];
    var currentIndex = currentOutfitIndex[characterName];
    var nextIndex = (currentIndex + 1) % outfits.length;

    // 更新当前换装索引
    currentOutfitIndex[characterName] = nextIndex;
    var newModel = outfits[nextIndex];

    // 更新全局模型索引
    var modelIndex = models.indexOf(newModel);
    if (modelIndex !== -1) {
        currentModelIndex = modelIndex;
    }

    // 加载新模型
    loadlive2d("live2d", newModel);
    console.log("角色" + characterName + "换装到:", newModel);
    saveState(); // 保存状态
}

// 实现看板娘的拖拽功能
$('#landlord').draggable({
    cursor: 'move',
    containment: false, // 允许拖动超出父容器边界

    // 拖动开始,暂停动画和过渡效果
    start: function () {
        $(this).find('.message').css('animation-play-state', 'paused');
        $(this).css('transition', 'none');
    },
    // 拖动结束,恢复动画和过渡效果
    stop: function () {
        $(this).find('.message').css('animation-play-state', 'running');
        $(this).css('transition', 'all .3s ease-in-out');
    }
});

// 页面加载完成后初始化
$(document).ready(function () {
    
    // 添加清除缓存功能（按Shift+Ctrl+Alt+Enter）
    $(document).keydown(function(e) {
        if (e.shiftKey && e.ctrlKey && e.altKey && e.which === 13) {
            clearCache();
            alert('看板娘缓存已清除！页面将重新加载。');
            location.reload();
        }
    });
    
    // 添加控制台命令提示
    console.log('刷新页面后看板娘和换装状态将被记住');
    console.log('按 Shift+Ctrl+Alt+Enter 可以清除看板娘缓存状态');
});