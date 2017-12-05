var exec = require('cordova/exec');

window.setOverrideUrlCallback = function(callback) {
    exec(callback, callback, this.serviceName, "setCallback", []);
};
