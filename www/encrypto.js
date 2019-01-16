var exec = require('cordova/exec');

var Encrypto = function () {};

Encrypto.prototype.decrypt = function (privateKey, message, success, error) {
    exec(success, error, 'Encrypto', 'decrypt', [privateKey, message]);
};

var Encrypto = new Encrypto();

module.exports = Encrypto;
