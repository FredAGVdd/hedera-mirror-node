/*-
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2021 Hedera Hashgraph, LLC
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

'use strict';

const protoToName = {
  2: 'CONTRACT',
  3: 'ED25519',
  4: 'RSA_3072',
  5: 'ECDSA_384',
  6: 'ECDSA_SECP256K1',
};

const UNKNOWN = 'UNKNOWN';

const getName = (protoId) => {
  return protoToName[protoId] || UNKNOWN;
};

module.exports = {
  getName,
};