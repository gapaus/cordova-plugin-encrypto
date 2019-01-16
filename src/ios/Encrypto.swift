@objc(AES256) class Encrypto : CDVPlugin {
    
    func decrypt(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(
            status: CDVCommandStatus_ERROR,
            messageAs: "Error occurred while performing Decryption"
        )
        
        let privateKey = command.arguments[0] as? String ?? ""
        let message = command.arguments[1] as? String ?? ""

        let decrypted = "test";
        
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: decrypted)
        
        self.commandDelegate!.send(
            pluginResult,
            callbackId: command.callbackId
        )
    }
    
}
