import SwCrypt

@objc(Encrypto) class Encrypto: CDVPlugin {

    func decrypt(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(
                status: CDVCommandStatus_ERROR,
                messageAs: "Error occurred while performing Decryption"
        )

        let privateKey = command.arguments[0] as? String ?? ""
        let message = command.arguments[1] as? String ?? ""

        let privateKeyDER = try! SwKeyConvert.PrivateKey.pemToPKCS1DER(privateKey)

        let messageArr = Data(base64Encoded: message)!;
        let tag = Data(count: 0);

        let encSessionKey = messageArr[0..<256]
        let sessionKey = try! CC.RSA.decrypt(encSessionKey, derKey: privateKeyDER, tag: tag, padding: .oaep, digest: .sha1)

        let encIv = messageArr[256..<512]
        let iv = try! CC.RSA.decrypt(encIv, derKey: privateKeyDER, tag: tag, padding: .oaep, digest: .sha1)

        let encData = messageArr[512..<messageArr.count]
        let decodedData = try! CC.crypt(.decrypt, blockMode: .cbc, algorithm: .aes, padding: .pkcs7Padding, data: encData, key: sessionKey.0, iv: iv.0)
        let decrypted = String(decoding: decodedData, as: UTF8.self)
//        print(decodedData.map {
//            String(format: "%02hhx", $0)
//        }.joined());

        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: decrypted)

        self.commandDelegate!.send(
                pluginResult,
                callbackId: command.callbackId
        )
    }

}
