package com.excella.reactor.config;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.text.ParseException;
import java.util.Map;

@Slf4j
public class JwtTokenConverter extends JwtAccessTokenConverter {

  private RSAKey recipientJWK, recipientPublicJWK;

  public JwtTokenConverter() {
    try {
      recipientJWK = new RSAKeyGenerator(2048).keyID("456").keyUse(KeyUse.ENCRYPTION).generate();
      recipientPublicJWK = recipientJWK.toPublicJWK();
    } catch (JOSEException e) {
      log.error("Could not create token", e);
    }
  }

  @Override
  protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    String jwt = super.encode(accessToken, authentication);

    try {
      // jwt is already signed at this point (by JwtTokenConverter)
      SignedJWT parsed = SignedJWT.parse(jwt);

      // Create JWE object with signed JWT as payload
      JWEObject jweObject = new JWEObject(
          new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM).contentType("JWT") // required
              .build(),
          new Payload(parsed));

      // Encrypt with the recipient's public key
      jweObject.encrypt(new RSAEncrypter(recipientPublicJWK));

      // Serialise to JWE compact form
      return jweObject.serialize();
    } catch (Exception e) {
      log.error("Could not encode", e);
    }

    return jwt;
  }

  @Override
  protected Map<String, Object> decode(String token) {
    try {
      // basically treat the incoming token as an encrypted JWT
      EncryptedJWT parse = EncryptedJWT.parse(token);
      // decrypt it
      RSADecrypter dec = new RSADecrypter(recipientJWK);
      parse.decrypt(dec);
      // content of the encrypted token is a signed JWT (signed by
      // JwtAccessTokenConverter)
      SignedJWT signedJWT = parse.getPayload().toSignedJWT();
      // pass on the serialized, signed JWT to JwtAccessTokenConverter
      return super.decode(signedJWT.serialize());

    } catch (ParseException e) {
      log.error("Could not parse", e);
    } catch (JOSEException e) {
      log.error("Error decoding JWT", e);
    }

    return super.decode(token);
  }
}