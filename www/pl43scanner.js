var exec = require('cordova/exec');

function PL43Scanner() {
    
};

PL43Scanner.prototype.scan = function (successCallback, errorCallback, config) {
    /*
    if (config instanceof Array) {
        // do nothing
    } else {
        if (typeof (config) === 'object') {
            config = [config];
        } else {
            config = [];
        }
    }
    */
    if (errorCallback == null) {
        errorCallback = function () {
        };
    }
    if (typeof errorCallback != "function") {
        console.log("PL43Scanner.scan failure: failure parameter not a function");
        return;
    }
    if (typeof successCallback != "function") {
        console.log("PL43Scanner.scan failure: success callback parameter must be a function");
        return;
    }
    exec(successCallback, errorCallback, 'PL43Scanner', 'scan', []);
};

var pl43scanner = new PL43Scanner();
module.exports = pl43scanner;