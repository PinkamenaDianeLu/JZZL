// 登录方式切换
function checkType(param) {
    $('.rightItemBox').hide();
    $('.' + param + 'Box').show();

    $('.typeSpan').removeClass('activeType');
    $('.typeText').removeClass('activeText');
    $('.' + param + 'Type').addClass('activeType');
    $('.' + param + 'Typetext').addClass('activeText');
}

var canvas = document.getElementById('canvas'),
    ctx = canvas.getContext('2d'),
    w = canvas.width = window.innerWidth,
    h = canvas.height = window.innerHeight,

    hue = 217,
    stars = [],
    count = 0,
    maxStars = 1200;

var canvas2 = document.createElement('canvas'),
    ctx2 = canvas2.getContext('2d');
canvas2.width = 100;
canvas2.height = 100;
var half = canvas2.width / 2,
    gradient2 = ctx2.createRadialGradient(half, half, 0, half, half, half);
gradient2.addColorStop(0.025, '#fff');
gradient2.addColorStop(0.1, 'hsl(' + hue + ', 61%, 33%)');
gradient2.addColorStop(0.25, 'hsl(' + hue + ', 64%, 6%)');
gradient2.addColorStop(1, 'transparent');

ctx2.fillStyle = gradient2;
ctx2.beginPath();
ctx2.arc(half, half, half, 0, Math.PI * 2);
ctx2.fill();

// End cache

function random(min, max) {
    if (arguments.length < 2) {
        max = min;
        min = 0;
    }

    if (min > max) {
        var hold = max;
        max = min;
        min = hold;
    }

    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function maxOrbit(x, y) {
    var max = Math.max(x, y),
        diameter = Math.round(Math.sqrt(max * max + max * max));
    return diameter / 2;
}

var Star = function () {

    this.orbitRadius = random(maxOrbit(w, h));
    this.radius = random(60, this.orbitRadius) / 12;
    this.orbitX = w / 2;
    this.orbitY = h / 2;
    this.timePassed = random(0, maxStars);
    this.speed = random(this.orbitRadius) / 900000;
    this.alpha = random(2, 10) / 10;

    count++;
    stars[count] = this;
}

Star.prototype.draw = function () {
    var x = Math.sin(this.timePassed) * this.orbitRadius + this.orbitX,
        y = Math.cos(this.timePassed) * this.orbitRadius + this.orbitY,
        twinkle = random(10);

    if (twinkle === 1 && this.alpha > 0) {
        this.alpha -= 0.05;
    } else if (twinkle === 2 && this.alpha < 1) {
        this.alpha += 0.05;
    }

    ctx.globalAlpha = this.alpha;
    ctx.drawImage(canvas2, x - this.radius / 2, y - this.radius / 2, this.radius, this.radius);
    this.timePassed += this.speed;
}

for (var i = 0; i < maxStars; i++) {
    new Star();
}

function animation() {
    ctx.globalCompositeOperation = 'source-over';
    ctx.globalAlpha = 0.8;
    ctx.fillStyle = 'hsla(' + hue + ', 64%, 30%, 1)';
    // ctx.fillStyle = 'hsla(' + hue + ', 50%, 25%, 1)';
    // ctx.fillStyle = 'hsla(' + hue + ', 40%, 28%, 1)';
    // ctx.fillStyle = 'hsla(' + hue + ', 70%, 25%, 1)';

    ctx.fillRect(0, 0, w, h)

    ctx.globalCompositeOperation = 'lighter';
    for (var i = 1, l = stars.length; i < l; i++) {
        stars[i].draw();
    }
    ;

    window.requestAnimationFrame(animation);
}

animation();

var logIn = (function () {

    function loadMessageFactory() {
        const loginMessage = function (oriName, oriPwd, key) {
            this.oriName = oriName;
            this.oriPwd = oriPwd;
            this.key = key;
        }
        let loginMessageObj = new loginMessage();
        loginMessageObj.key = '123';
        loginMessageObj.oriPwd = $('#password').val();
        loginMessageObj.oriName = $('#username').val();
        return JSON.stringify(loginMessageObj);
    }

    function login() {
        $.post({
            url: '/Salt/getSalt',
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    sessionStorage.salt = reV.value;//加密的盐
                    sessionStorage.version = reV.version;//当前版本
                    sessionStorage.groupcode = reV.groupcode;//当前地市

                    //验证登录
                    $.post({
                        url: '/Login/login',
                        data: {loginMessage: loadMessageFactory()},
                        success: (re) => {
                            const reV = JSON.parse(re);
                            switch (reV.message) {
                                case 'success':
                                    //正常登录成功
                                    location.href = '../model/ajcx/ajcx.html';
                                    sessionStorage.username = oriName;
                                    break
                                case 'urlLogin_success':
                                    //外部跳转
                                    let urlP = window.btoa(reV.value + sessionStorage.salt);
                                    location.href = '../model/ajcx/sjjl.html?id=' + urlP;
                                    sessionStorage.username = oriName;
                                    //c保存案综传递来的角色
                                    console.log(reV.azParam)
                                    if (sessionStorage.version ==='province'||sessionStorage.version === 'provinceTest'){
                                        sessionStorage.js=reV.azParam.js;
                                    }
                                    break;
                                case 'deny':
                                    layer.alert('用户名或密码错误！')
                                    break;
                                case 'urlLogin_deny':
                                    layer.alert('传递的参数有误！')
                                    break;
                                default:
                                    layer.alert('缺少登录所需要的必要值！');
                            }
                        }
                    });
                } else {
                    //TODO MrLu 2020/9/27 不允许登录
                }
            }
        });
    }


    function _logIn() {
    }

    _logIn.prototype = {
        login
    }
    return _logIn;
})()


$(function () {

    let lj=new logIn();
    //绑定登录按钮监听
    $("#loginBtn").unbind("click").click(function () {
        lj.login();
    });

    //监听全页面
    window.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') {
            //回车按钮
            lj.login();
        }
    });
    //监听密码输入框的 <-  按钮
    document.getElementById('password').addEventListener('keydown', function (e) {
        if (e.key === 'Backspace') {
            //这里不能使用$(this)
            $('#password').val('');
        }
    });
})

