# Live2D 看板娘

原项目出自：恶魔萝莉控 [阅读原文](https://nocilol.me/archives/lab/add-dynamic-poster-girl-with-live2d-to-your-blog-02/)

项目基于 [galnetwen/Live2D](https://github.com/galnetwen/Live2D) 修改而来，添加了模型切换按钮和可拖拽看板娘功能。调整了模型的初始位置和画布大小，使得模型能够完整显示。

Live2D模型来源：[oh-my-live2d/live2d-models](https://github.com/oh-my-live2d/live2d-models)

## 添加的功能
### 1. 模型切换按钮

在看板娘的左侧添加了一个模型切换按钮，点击按钮可以切换看板娘的模型。

**实现代码**（位于 `live2d/js/start.js` 文件）：

```javascript
// 模型列表，包含所有可用模型
var models = [
    "/live2d/model/hallo/model.json",
    "/live2d/model/date/model.json",
    // 更多模型...
];

// 初始模型索引
var currentModelIndex = 0;

// 初始加载模型
loadlive2d("live2d", models[currentModelIndex]);

// 切换看板娘按钮点击事件
$('.switch-button').click(function () {
    // 循环切换模型索引
    currentModelIndex = (currentModelIndex + 1) % models.length;
    // 加载新模型
    loadlive2d("live2d", models[currentModelIndex]);
    // 控制台输出当前模型信息
    console.log(currentModelIndex, models[currentModelIndex]);
});
```

**功能说明**：
- 定义了一个包含所有模型路径的数组 `models`
- 使用 `currentModelIndex` 变量跟踪当前显示的模型
- 点击 `.switch-button` 元素时，切换到下一个模型
- 使用取余运算 `%` 实现模型列表的循环切换

### 2. 可拖拽看板娘

看板娘可以通过鼠标拖拽来移动位置，方便在不同位置展示。

**实现代码**（位于 `live2d/js/start.js` 文件）：

```javascript
// 实现看板娘的拖拽功能
$('#landlord').draggable({
    cursor: 'move', // 拖动时的鼠标样式
    containment: false, // 允许拖动超出父容器边界

    // 拖动开始,暂停动画和过渡效果
    start: function (event, ui) {
        $(this).find('.message').css('animation-play-state', 'paused'); // 暂停消息动画
        $(this).css('transition', 'none'); // 移除过渡效果，提高拖动性能
    },
    // 拖动结束,恢复动画和过渡效果
    stop: function (event, ui) {
        $(this).find('.message').css('animation-play-state', 'running'); // 恢复消息动画
        $(this).css('transition', 'all .3s ease-in-out'); // 恢复过渡效果
    }
});
```

**功能说明**：
- 使用 jQuery UI 的 `draggable()` 方法实现拖拽功能
- 设置拖动时的鼠标样式为 "move"
- 允许看板娘拖动超出父容器边界
- 拖动开始时暂停动画和过渡效果，提高拖动性能
- 拖动结束后恢复动画和过渡效果，保持视觉流畅性

**依赖**：
- 需要引入 jQuery UI 库才能使用 `draggable()` 方法
- 在 `demo.html` 文件中已包含：`<script src="/live2d/js/jquery-ui.min.js"></script>`

## 自定义配置
### 1. 自定义消息

编辑 `live2d/message.json` 文件，添加或修改交互消息：

```json
{
    "mouseover": [
        {
            // 当鼠标悬停在标题链接上时，看板娘可以说："要看看 【标题】 么？"
            "selector": ".title a",
            "text": ["要看看 {text} 么？"]
        },
        {
            "selector": ".searchbox",
            "text": ["在找什么东西呢，需要帮忙吗？"]
        }
    ],
    "click": [
        {
            // 点击看板娘时
            "selector": "#landlord #live2d",
            "text": ["不要动手动脚的！", "真…真的是不知羞耻！"]
        }
    ]
}
```
### 2.调整一言api输出间隔

编辑 `live2d/js/message.js` 文件，修改 `setInterval` 函数的第二个参数：

```javascript
// 15秒输出一次
window.setInterval(showHitokoto, 15000);

// 修改为5秒输出一次
window.setInterval(showHitokoto, 5000);
```

### 3. 添加新模型

1. 将新模型文件夹放入 `live2d/model/` 目录
2. 在 HTML 文件的模型列表中添加新模型路径

```javascript
var models = [
    "/live2d/model/hallo/model.json",
    "/live2d/model/new-model/model.json" // 新模型
];
```

### 4. 画布尺寸修改

在 HTML 文件中修改 canvas 标签的 width 和 height 属性，调整画布大小：

```html
<!-- 默认尺寸 -->
<canvas id="live2d" width="340" height="850" class="live2d"></canvas>

<!-- 自定义尺寸示例 -->
<canvas id="live2d" width="400" height="600" class="live2d"></canvas>
```

### 5. 画布初始定位

编辑 `live2d/css/live2d.css` 文件，修改 `#landlord` 选择器的样式，调整画布初始位置：

```css
#landlord {
    position: fixed; /* 固定定位 */
    left: 0; /* 水平位置：左侧 */
    top: calc(50% - 280px); /* 垂直位置：居中后根据模型高度调整 */
}
```
### 6. 模型的位置大小修改

通过修改`\live2d\model\modelname\model.json`文件中的`layout`参数，调整模型在画布中的位置和大小。

```javascript
{
    "layout": {
        "center_x": 0.0,// 模型中心x轴位置
	    "center_y": -0.6,// 模型中心y轴位置
	    "width": 2.5// 模型宽度
    }
}
```