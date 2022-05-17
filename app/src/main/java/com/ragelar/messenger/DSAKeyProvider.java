package com.ragelar.messenger;





import org.bouncycastle.crypto.prng.FixedSecureRandom;

import java.security.*;

public class DSAKeyProvider {

    PrivateKey privateKey;
    PublicKey publicKey;

    DSAKeyProvider(byte[] seed){
        KeyPairGenerator keyPairGenerator = null;
        SecureRandom random = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("DSA");
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        keyPairGenerator.initialize(1024, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
