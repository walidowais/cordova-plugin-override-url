var exec = require('cordova/exec');
var platform = require('cordova/platform');

module.exports = {
    setCallback: function(count, callback, error) {
        var defaultedCount = count || 1;
        exec(callback, error, 'OverrideUrl', 'setCallback', [ defaultedCount ]);
    },

    /**
     * Causes the device to beep.
     * On Android, the default notification ringtone is played "count" times.
     *
     * @param {Integer} count       The number of beeps.
     */
    beep: function (count) {
        var defaultedCount = count || 1;
        exec(null, null, 'OverrideUrl', 'beep', [ defaultedCount ]);
    }
};
