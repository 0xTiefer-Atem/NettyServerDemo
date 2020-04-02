function new_project() {
    $("#new_project").show();
    $("#home_page").hide();
}


function cancel() {
    $("#home_page").show();
    $("#new_project").hide();
}

function cancel_open(){
    $("#n_file").hide();
    $("#new_file").show();
}

function n_file() {
    $("#new_file").hide();
    $("#n_file").show();
}

//新建输入框是否为空
//新建文件夹
function n_project(){
    if (document.getElementById('new_project_form_name').value==='') {
        alert("Please input Project Name!");
        document.getElementById('new_project_form_location').value='';
        console.log("Project Name is null!")
    }else if (document.getElementById('new_project_form_location').value===''){
        alert("Please input Project Location!");
        document.getElementById('new_project_form_name').value='';
        console.log("Project Location is null!")
    } else {
        console.log("true");
        $.ajax({
            async:true,
            type:"POST",
            url:"http://127.0.0.1:9999/createdirectory",
            contentType:"application/x-www-form-urlencoded ; charset=utf-8",
            data:{
                file_name:$("#new_project_form_name").val(),
                file_path:$("#new_project_form_location").val()
            },
            dataType:"json",
            success:function (response) {
                console.log(response.message+'xxx');
                var file_name=$("#new_project_form_name").val();
                var file_path=$("#new_project_form_location").val();
                localStorage.setItem("new_project_form_name",file_name);
                localStorage.setItem("new_project_form_location",file_path);
                console.log(localStorage);
                // window.location.href="shell.html";
                $("#new_project").hide();
                $("#ide").show();
            },
            error: function(jqXHR, error, errorThrown) {
                console.log(jqXHR.status);
                console.log(jqXHR.responseText);
                console.log(jqXHR.readyState);
                console.log(jqXHR.statusText);
            }
        });
    }
}

//创建shell文件
function new_file() {
    if (document.getElementById('form_name').value==='') {
        alert("File input Project Name!");
        document.getElementById('form_location').value='';
        console.log("File Name is null!")
    }else if (document.getElementById('form_location').value===''){
        alert("Please input File Location!");
        document.getElementById('form_name').value='';
        console.log("File Location is null!")
    } else {
        console.log("ok");
        $.ajax({
            async: true,
            type: "POST",
            url: "http://127.0.0.1:9999/createfile",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                file_name: $("#form_name").val(),
                file_path: $("#form_location").val()
                // file_name:'testestst',
                // file_path:'/home/zk/'
            },
            dataType: "json",
            success: function () {
                    console.log("ok");
                    var file=$("#form_name").val();
                    var file_p=$("#form_location").val();
                    localStorage.setItem("form_name",file);
                    localStorage.setItem("form_location",file_p);
                    window.location.href="shell.html";
            }
        });
    }
}


function new_code_file() {
    if (document.getElementById('form_name1').value==='') {
        alert("File input Project Name!");
        document.getElementById('form_location1').value='';
        console.log("File Name is null!")
    }else if (document.getElementById('form_location1').value===''){
        alert("Please input File Location!");
        document.getElementById('form_name1').value='';
        console.log("File Location is null!")
    } else {
        console.log("ok");
        $.ajax({
            async: true,
            type: "POST",
            url: "http://127.0.0.1:9999/createfile",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                file_name: $("#form_name1").val(),
                file_path: $("#form_location1").val()
                // file_name:'testestst',
                // file_path:'/home/zk/'
            },
            dataType: "json",
            success: function () {
                    console.log("ok");
                    var file=$("#form_name1").val();
                    var file_p=$("#form_location1").val();
                    localStorage.setItem("form_name1",file);
                    localStorage.setItem("form_location1",file_p);
                    window.location.href="code.html";
            }
        });
    }
}
//创建codefile

//shell>next
function s_next() {
    $.ajax({
        async:true,
        type:"POST",
        url:"http://127.0.0.1:9999/upload",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
        data:{
            file_name:localStorage.getItem("form_name"),
            file_path:localStorage.getItem("form_location"),
            username:localStorage.getItem("name")
        },
        dataType:"json",
        success:function () {
            console.log("OK!");
            // window.location.href="code.html";
            $("#shell").hide();
            $("#n_file").show();
        }
    })
}

//打开文件
function opens() {
    console.log("123");
    $.ajax({
        async:true,
        type:"POST",
        url:"http://127.0.0.1:9999/openfile",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
        data:{
            file_name:$("#form_name").val(),
            file_path:$("#form_location").val()
        },
        dataType:"json",
        success:function (data) {
            $("#shelltextarea").html('shell',data['shell']);
            $("#codeTextarea").html('code',data['code']);
        }
    });
}

//保存文件
function saves() {
    console.log("123");
    $.ajax({
        async:true,
        type:"POST",
        url:"http://127.0.0.1:9999/savefile",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
        data:{
            file_name:localStorage.getItem("form_name"),
            file_path:localStorage.getItem("form_location"),
            shell:$("#shelltextarea").val()
            // code:$("#codeTextarea").val()
        },
        dataType:"json",
        success:function () {
            console.log("OK!");
        }
    });
}
function saves1() {
    console.log("123");
    $.ajax({
        async:true,
        type:"POST",
        url:"http://127.0.0.1:9999/savefile",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
        data:{
            file_name:localStorage.getItem("form_name1"),
            file_path:localStorage.getItem("form_location1"),
            // shell:$("#shelltextarea").val(),
            code:$("#codeTextarea").val()
        },
        dataType:"json",
        success:function () {
            console.log("OK!");
        }
    });
}


//上传code
function uploads() {
    console.log("123");
    $.ajax({
        async:true,
        type:"POST",
        url:"http://127.0.0.1:9999/upload",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
        data:{
            file_name:localStorage.getItem("form_name1"),
            file_path:localStorage.getItem("form_location"),
            username:localStorage.getItem("form_name")
        },
        dataType:"json",
        success:function () {
            console.log("OK!");
        }
    });
}
//
function open_project() {
    $.ajax({
        async:true,
        type:"POST",
        url:"http:127.0.0.1:9999/isdir",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
        data:{
            file_path:"",
        },
        dataType:"json",
        success:function (data) {
            if (data==='true'){

            }
        }
    })
}

