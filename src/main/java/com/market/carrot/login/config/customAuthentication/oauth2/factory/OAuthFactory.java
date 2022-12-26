package com.market.carrot.login.config.customAuthentication.oauth2.factory;

import com.market.carrot.login.config.customAuthentication.oauth2.providerType.CheckOAuthProvider;
import com.market.carrot.login.config.customAuthentication.oauth2.providerType.ProviderByFacebook;
import com.market.carrot.login.config.customAuthentication.oauth2.providerType.ProviderByGoogle;

public class OAuthFactory implements ProviderFactory {

  @Override
  public CheckOAuthProvider getProvider(String provider) {
    if (provider.equals("google")) {
      return new ProviderByGoogle();

    } else if (provider.equals("facebook")) {
      return new ProviderByFacebook();
    }

    return null;
  }
}
