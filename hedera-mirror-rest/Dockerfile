FROM node:16.13.0-alpine3.14
LABEL maintainer="mirrornode@hedera.com"

# Setup
WORKDIR /home/node/app/
RUN chown -R node:node .
COPY --chown=node:node . ./
USER node

# Build
ENV NODE_ENV production
RUN npm ci --only=production && npm cache clean --force --loglevel=error

# Run
EXPOSE 5551
CMD ["node", "server.js"]
