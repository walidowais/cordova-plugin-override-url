var exec = require('cordova/exec');
var platform = require('cordova/platform');

OverrideUrl = {
    setCallback: function(count, callback, error) {
        var defaultedCount = count || 1;
        exec(callback, error, 'OverrideUrl', 'setCallback', [ defaultedCount ]);
    },
};

// prime it. setTimeout so that proxy gets time to init
window.setTimeout(function () {
    exec(function (res) {
        if (typeof res == 'object') {
            console.log("overrideUrl event: " + JSON.stringify(res));
            if (res.type == 'overrideUrl') {
                cordova.fireWindowEvent('overrideUrl');
            }
        } else {
            console.log("overrideUrl event idk" + res);
            StatusBar.isVisible = res;
        }
    }, null, "OverrideUrl", "_ready", []);
}, 0);

module.exports = OverrideUrl;
