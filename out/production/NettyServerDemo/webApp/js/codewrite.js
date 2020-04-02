var fs=require("fs");
var fd=fs.openSync("aaa.html","a",{start:20});
fs.writeSync(fd,"wwww.baidu.com");
fs.close(fd);