var exec = require('cordova/exec');  
module.exports = {  
  
    /** 
     * һ��5������ 
       ��һ�� :�ɹ��ص� 
       �ڶ��� :ʧ�ܻص� 
       ������ :��Ҫ���õ������������(��config.xml������ �Ժ�������ὲ��) 
       ���ĸ� :���õķ�����(һ����������ж������ �������������) 
       ����� :���ݵĲ���  ��json�ĸ�ʽ 
     */  
    scan: function(mills,callback,err) {  
        exec(function(winParam){  
            callback(winParam);  
        }, function(errParam){  
            err(errParam);  
        }, "pl43scanner", "scan", [mills]);  
    }
};