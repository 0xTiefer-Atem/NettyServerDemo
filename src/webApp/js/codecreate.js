var fs=require("fs");
//打开文件
var fd=fs.openSync("aaa.html","w");
//写入文件
fs.writeSync(fd,"<!DOCTYPE html>\n" +
    "<html lang=\"en\">\n" +
    "<head>\n" +
    "    <meta charset=\"UTF-8\">\n" +
    "    <title>Title</title>\n" +
    "</head>\n"+
    "<body>\n\n"+
    "</body>\n"+
    "</html>");
//关闭并保存文件(在服务器中运行，不停，故打开文件写入后需要关闭，减少内存占用)
fs.close(fd);
