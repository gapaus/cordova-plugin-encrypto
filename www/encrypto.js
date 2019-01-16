var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var utils = require('cordova/utils');
var exec = require('cordova/exec');
var cordova = require('cordova');

channel.createSticky('onCordovaInfoReady');
// Tell cordova channel to wait on the CordovaInfoReady event
channel.waitForInitialization('onCordovaInfoReady');

/**
 * @constructor
 */
function Encrypto () {

    var me = this;

    channel.onCordovaReady.subscribe(function () {
        me.decrypt(function (privateKey, message) {

            channel.onCordovaInfoReady.fire();
        }, function (e) {
            me.available = false;
            utils.alert('[ERROR] Error initializing Cordova: ' + e);
        });
    });
}

/**
 * Decrypt message
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
Encrypto.prototype.decrypt = function (successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Encrypto.decrypt', arguments);
    exec(successCallback, errorCallback, 'Encrypto', 'decrypt', []);
};

module.exports = new Encrypto();
