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

// external libraries
const log4js = require('log4js');
const Pool = require('pg-pool');

// local
const config = require('./config');

const logger = log4js.getLogger();

// support pull from db to verify diff, then bulk update. Note dryRun phase
const pool = new Pool({
  user: config.db.username,
  host: config.db.host,
  database: config.db.name,
  password: config.db.password,
  port: config.db.port,
});

const getEntityObFromString = (id) => {
  let entityIdObj = {
    shard: 0,
    realm: 0,
    num: 0,
  };

  const idParts = id.split('.');
  if (idParts.length === 1) {
    entityIdObj.num = id;
  } else if (idParts.length === 3) {
    entityIdObj.shard = idParts[0];
    entityIdObj.realm = idParts[1];
    entityIdObj.num = idParts[2];
  } else {
    throw Error('Id format is incorrect');
  }

  return entityIdObj;
};

const getEntity = async (id) => {
  const entityIdObj = getEntityObFromString(id);

  logger.trace(`getEntity for ${id}`);

  const paramValues = [entityIdObj.shard, entityIdObj.realm, entityIdObj.num];
  const entityFromDb = await pool.query(
    'select * from t_entities_bkup where entity_shard = $1 and entity_realm = $2 and entity_num = $3',
    paramValues
  );

  // logger.info(`Retrieve entity info ${JSON.stringify(entityFromDb.rows[0])} for ${id}`);
  return entityFromDb.rows[0];
};

const updateEntity = async (entity) => {
  const paramValues = [
    entity.auto_renew_period,
    entity.deleted,
    entity.ed25519_public_key_hex,
    entity.exp_time_ns,
    entity.key,
    entity.proxy_account_id,
    entity.id,
  ];

  await pool.query(
    `update t_entities_bkup
       set auto_renew_period      = $1,
           deleted                = $2,
           ed25519_public_key_hex = $3,
           exp_time_ns            = $4,
           key                    = $5,
           proxy_account_id       = $6
       where id = $7`,
    paramValues
  );

  logger.debug(`Updated entity ${entity.id}`);
};

module.exports = {
  getEntity,
  updateEntity,
};