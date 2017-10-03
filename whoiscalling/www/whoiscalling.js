var exec = require('cordova/exec');

exports.registerCallEvents = function(arg0, success, error) {
    exec(success, error, "whoiscalling", "registerCallEvents", [arg0]);
};
