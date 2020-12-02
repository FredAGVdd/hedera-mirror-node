package com.hedera.datagenerator.sdk.supplier.consensus;

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

import com.google.common.primitives.Longs;
import java.security.SecureRandom;
import java.time.Instant;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.hedera.datagenerator.common.Utility;
import com.hedera.datagenerator.sdk.supplier.TransactionSupplier;
import com.hedera.hashgraph.sdk.consensus.ConsensusMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;

@Data
public class ConsensusSubmitMessageTransactionSupplier implements TransactionSupplier<ConsensusMessageSubmitTransaction> {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Min(1)
    private long maxTransactionFee = 1_000_000;

    private String message = StringUtils.EMPTY;

    @Min(1)
    private int messageSize = 256;

    @NotBlank
    private String topicId;

    @Override
    public ConsensusMessageSubmitTransaction get() {

        return new ConsensusMessageSubmitTransaction()
                .setMaxTransactionFee(maxTransactionFee)
                .setMessage(getMessage())
                .setTopicId(ConsensusTopicId.fromString(topicId))
                .setTransactionMemo(Utility.getMemo("Mirror node submitted test message"));
    }

    private String getMessage() {
        String encodedTimestamp = Utility.getEncodedTimestamp();
        //If a custom message is entered, append the timestamp to the front and leave the message unaltered
        if (StringUtils.isNotBlank(message)) {
            return encodedTimestamp + "_" + message;
        }
        //Generate a message from the timestamp and a random alphanumeric String
        byte[] timeRefBytes = Longs.toByteArray(Instant.now().toEpochMilli());
        int additionalBytes = messageSize <= timeRefBytes.length ? 0 : messageSize - timeRefBytes.length;
        String randomAlphanumeric = RandomStringUtils.random(additionalBytes, 0, 0, true, true, null, RANDOM);
        return encodedTimestamp + randomAlphanumeric;
    }
}
