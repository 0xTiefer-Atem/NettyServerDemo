function register() {
    $("#div").hide();
    $("#register_div").show();
}

function login() {
    $("#register_div").hide();
    $("#div").show();
}

//登陆页面输入框是否为空
function check_login() {
    var username = document.getElementById("username");
    var email = document.getElementById("email");
    var pass = document.getElementById("password");
    if (document.getElementById('username').value===''){
        console.log("username is null");
        $("#div .login").val('');
    }else if (document.getElementById('email').value===''){
        console.log("email is null");
        $("#div .login").val('');
    } else if(document.getElementById('password').value===''){
        console.log("password is null");
        $("#div .login").val('');
    }else {
        console.log("true");
        $.ajax({
            async:true,
            type:"POST",
            // url:"http://47.107.64.157:8088/login",
            url:"http://localhost:8088/login",
            data:"pass="+pass.value+"&email="+email.value+"&count="+username.value,
            dataType:"json",
            success:function (response) {
                if (response['status']==='ok'){
                    var name=$("#username").val();
                    localStorage.setItem("name", name);
                    //跳到写一个页面
                    window.location.href='index.html';
                } else {
                    alert("登录失败，请重新登陆！");
                    $("#div .login").val('');
                }
            }
        })
    }
}
//注册页面输入框是否为空
function check_register() {
    var username = document.getElementById("r_username");
    var email = document.getElementById("r_email");
    var password = document.getElementById("r_password");
    if (document.getElementById('r_username').value===''){
        console.log("username is null");
        $("#register_div .register").val('');
    }else if (document.getElementById('r_email').value===''){
        console.log("email is null");
        $("#register_div .register").val('');
    } else if(document.getElementById('r_password').value===''){
        console.log("password is null");
        $("#register_div .register").val('');
    }else if(document.getElementById('r_r_password').value===''){
        console.log("password is null");
        $("#register_div .register").val('');
    }
    else {
        console.log("true");
        $.ajax({
            async:true,
            type:"POST",
            url:"http://localhost:8088/register",
            // url:"http://47.107.64.157:8088/register",
            data:"pass="+password.value+"&email="+email.value+"&count="+username.value,
            dataType:"json",
            success:function (response) {
                if (response['status'] === 'ok') {
                    alert("请前往邮箱进行验证！");
                } else {
                    alert("邮件发送失败，请重试！");
                }
            }
        })
    }
}

