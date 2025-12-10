// 仿知乎背景组件
class ZhihuBackground {
    constructor(canvasId = 'canvas', options = {}) {
        this.canvasId = canvasId;
        this.defaults = {
            particleCount: 60,
            particleColor: 'rgba(204, 204, 204, 0.3)',
            lineColor: 'rgba(204, 204, 204, 0.3)',
            maxLineDistance: 150,
            mouseCircleColor: 'rgba(255, 77, 54, 0.6)',
            mouseCircleRadius: 8
        };
        this.options = { ...this.defaults, ...options };
        this.canvas = null;
        this.ctx = null;
        this.width = 0;
        this.height = 0;
        this.circles = [];
        this.currentCircle = null;
        this.animationId = null;
    }

    // 初始化组件
    init() {
        this.canvas = document.getElementById(this.canvasId);
        if (!this.canvas) {
            console.error(`Canvas element with id "${this.canvasId}" not found`);
            return false;
        }

        this.ctx = this.canvas.getContext('2d');
        this.resize();
        this.createCircles();
        this.createCurrentCircle();
        this.bindEvents();
        this.animate();
        
        console.log('ZhihuBackground initialized successfully');
        return true;
    }

    // 调整canvas大小
    resize() {
        this.width = this.canvas.width = window.innerWidth;
        this.height = this.canvas.height = window.innerHeight;
    }

    // 创建普通粒子
    createCircles() {
        this.circles = [];
        for (let i = 0; i < this.options.particleCount; i++) {
            this.circles.push(new Circle(
                Math.random() * this.width,
                Math.random() * this.height,
                this.options
            ));
        }
    }

    // 创建鼠标跟随粒子
    createCurrentCircle() {
        this.currentCircle = new CurrentCircle(0, 0, this.options);
    }

    // 绑定事件
    bindEvents() {
        // 窗口大小改变事件
        window.addEventListener('resize', () => this.resize());
        
        // 鼠标移动事件
        window.addEventListener('mousemove', (e) => {
            e = e || window.event;
            this.currentCircle.x = e.clientX;
            this.currentCircle.y = e.clientY;
        });
        
        // 鼠标移出窗口事件
        window.addEventListener('mouseout', () => {
            this.currentCircle.x = null;
            this.currentCircle.y = null;
        });
    }

    // 动画循环
    animate() {
        this.ctx.clearRect(0, 0, this.width, this.height);
        
        // 绘制和移动普通粒子
        for (let i = 0; i < this.circles.length; i++) {
            this.circles[i].move(this.width, this.height);
            this.circles[i].drawCircle(this.ctx);
            
            // 绘制粒子之间的连接线
            for (let j = i + 1; j < this.circles.length; j++) {
                this.circles[i].drawLine(this.ctx, this.circles[j]);
            }
        }
        
        // 绘制鼠标跟随粒子和连接线
        if (this.currentCircle.x !== null && this.currentCircle.y !== null) {
            this.currentCircle.drawCircle(this.ctx);
            for (let k = 0; k < this.circles.length; k++) {
                this.currentCircle.drawLine(this.ctx, this.circles[k]);
            }
        }
        
        this.animationId = requestAnimationFrame(() => this.animate());
    }

    // 销毁组件
    destroy() {
        if (this.animationId) {
            cancelAnimationFrame(this.animationId);
        }
        window.removeEventListener('resize', () => this.resize());
        window.removeEventListener('mousemove', () => {});
        window.removeEventListener('mouseout', () => {});
        console.log('ZhihuBackground destroyed');
    }
}

// 普通粒子类
class Circle {
    constructor(x, y, options) {
        this.x = x;
        this.y = y;
        this.r = Math.random() * 10;
        this._mx = Math.random();
        this._my = Math.random();
        this.options = options;
    }

    // 绘制粒子
    drawCircle(ctx) {
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r, 0, 360);
        ctx.closePath();
        ctx.fillStyle = this.options.particleColor;
        ctx.fill();
    }

    // 绘制连接线
    drawLine(ctx, _circle) {
        let dx = this.x - _circle.x;
        let dy = this.y - _circle.y;
        let d = Math.sqrt(dx * dx + dy * dy);
        
        if (d < this.options.maxLineDistance) {
            ctx.beginPath();
            ctx.moveTo(this.x, this.y);
            ctx.lineTo(_circle.x, _circle.y);
            ctx.closePath();
            ctx.strokeStyle = this.options.lineColor;
            ctx.stroke();
        }
    }

    // 移动粒子
    move(w, h) {
        this._mx = (this.x < w && this.x > 0) ? this._mx : (-this._mx);
        this._my = (this.y < h && this.y > 0) ? this._my : (-this._my);
        this.x += this._mx / 2;
        this.y += this._my / 2;
    }
}

// 鼠标跟随粒子类
class CurrentCircle extends Circle {
    constructor(x, y, options) {
        super(x, y, options);
    }

    // 绘制鼠标跟随粒子
    drawCircle(ctx) {
        ctx.beginPath();
        this.r = this.options.mouseCircleRadius;
        ctx.arc(this.x, this.y, this.r, 0, 360);
        ctx.closePath();
        ctx.fillStyle = this.options.mouseCircleColor;
        ctx.fill();
    }
}

// 初始化函数 - 兼容jQuery
if (typeof $ !== 'undefined') {
    $.fn.zhihuBackground = function(options) {
        return this.each(function() {
            const canvasId = this.id;
            const background = new ZhihuBackground(canvasId, options);
            background.init();
            $(this).data('zhihuBackground', background);
        });
    };
}

// 自动初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        if (document.getElementById('canvas')) {
            new ZhihuBackground('canvas').init();
        }
    });
} else {
    if (document.getElementById('canvas')) {
        new ZhihuBackground('canvas').init();
    }
}
