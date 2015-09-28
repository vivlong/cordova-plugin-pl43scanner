var exec = require('cordova/exec');

function PL43Scanner() {
    this.Encode = {
        TEXT_TYPE: "TEXT_TYPE",
        EMAIL_TYPE: "EMAIL_TYPE",
        PHONE_TYPE: "PHONE_TYPE",
        SMS_TYPE: "SMS_TYPE"
    };
};

PL43Scanner.prototype.scan = function (successCallback, errorCallback, config) {
    if (config instanceof Array) {
        // do nothing
    } else {
        if (typeof (config) === 'object') {
            config = [config];
        } else {
            config = [];
        }
    }
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
    exec(successCallback, errorCallback, 'PL43Scanner', 'scan', config);
};

var pl43scanner = new PL43Scanner();
module.exports = pl43scanner;