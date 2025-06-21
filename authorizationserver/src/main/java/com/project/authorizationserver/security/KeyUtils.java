package com.project.authorizationserver.security;

import com.nimbusds.jose.jwk.RSAKey;
import com.project.authorizationserver.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

@Slf4j
@Component
public class KeyUtils {

    private static final String RSA = "RSA";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${keys.private}")
    private String privateKey;

    @Value("${keys.public}")
    private String publicKey;

    public RSAKey getRSAKeyPair() {
        return generateRsaKeyPair(privateKey, publicKey);
    }

    private RSAKey generateRsaKeyPair(String privateKeyName, String publicKeyName) {
        KeyPair keyPair;
        var keyDirectory = Paths.get("src", "main", "resources", "keys");
        verifyKeyDirectory(keyDirectory);

        if(Files.exists(keyDirectory.resolve(privateKeyName)) && Files.exists(keyDirectory.resolve(publicKeyName))) {
            log.info("RSA key pair found in keys directory: {}, {}", privateKeyName, publicKeyName);

            try {
                var privateKeyFile = keyDirectory.resolve(privateKeyName).toFile();
                var publicKeyFile = keyDirectory.resolve(publicKeyName).toFile();
                var keyId = UUID.randomUUID().toString();

                var keyFactory = KeyFactory.getInstance(RSA);
                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

                log.info("KeyID: {}", keyId);
                return new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).keyID(keyId).build();
            } catch(Exception exception) {
                throw new ApiException(exception.getMessage());
            }
        } else {
            if(activeProfile.equalsIgnoreCase("prod")) {
                throw new ApiException("Public & Private Keys don't exist in production environment");
            }
        }
        log.info("Generating RSA KeyPair");
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();

            try(var fos = new FileOutputStream(keyDirectory.resolve(publicKeyName).toFile())) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fos.write(keySpec.getEncoded());
                return new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).keyID(UUID.randomUUID().toString()).build();
            }
        } catch(Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    private void verifyKeyDirectory(Path keyDirectory) {
        if(!Files.exists(keyDirectory)) {
            try {
                Files.createDirectories(keyDirectory);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException(exception.getMessage());
            }
        }
    }

}
