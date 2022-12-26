package com.market.carrot.login.config.customAuthentication.oauth2.factory;

import com.market.carrot.login.config.customAuthentication.oauth2.providerType.CheckOAuthProvider;

public interface ProviderFactory {

  CheckOAuthProvider getProvider(String provider);
}
