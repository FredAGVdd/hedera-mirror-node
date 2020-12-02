package com.hedera.datagenerator.sdk.supplier.token;

/*-
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2020 Hedera Hashgraph, LLC
 * ​
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ‍
 */

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;

import com.hedera.datagenerator.common.Utility;
import com.hedera.datagenerator.sdk.supplier.TransactionSupplier;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PublicKey;
import com.hedera.hashgraph.sdk.token.TokenCreateTransaction;

@Data
public class TokenCreateTransactionSupplier implements TransactionSupplier<TokenCreateTransaction> {

    private String adminKey;

    @Min(1)
    private int decimals = 10;

    private boolean freezeDefault = false;

    @Min(1)
    private int initialSupply = 1000000000;

    @Min(1)
    private long maxTransactionFee = 1_000_000_000;

    @NotBlank
    private String symbol = "HMNT";

    @NotBlank
    private String treasuryAccountId;

    @Override
    public TokenCreateTransaction get() {

        AccountId treasuryAccoundId = AccountId.fromString(treasuryAccountId);
        TokenCreateTransaction tokenCreateTransaction = new TokenCreateTransaction()
                .setAutoRenewAccount(treasuryAccoundId)
                .setDecimals(decimals)
                .setInitialSupply(initialSupply)
                .setFreezeDefault(freezeDefault)
                .setMaxTransactionFee(maxTransactionFee)
                .setName(symbol + "_name")
                .setSymbol(symbol)
                .setTransactionMemo(Utility.getMemo("Mirror node created test token"))
                .setTreasury(treasuryAccoundId);

        if (adminKey != null) {
            Ed25519PublicKey key = Ed25519PublicKey.fromString(adminKey);
            tokenCreateTransaction
                    .setAdminKey(key)
                    .setFreezeKey(key)
                    .setKycKey(key)
                    .setSupplyKey(key)
                    .setWipeKey(key);
        }

        return tokenCreateTransaction;
    }
}
