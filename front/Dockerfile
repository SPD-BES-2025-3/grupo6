FROM node:20-alpine

WORKDIR /app

COPY package.json ./
COPY package-lock.json* ./

RUN npm ci || npm install --legacy-peer-deps

COPY . .

ARG BASE_API_URL
ENV BASE_API_URL=$BASE_API_URL

RUN npm run build

# Instala o 'serve' para servir o conteúdo estático
RUN npm install -g serve

EXPOSE 3000

CMD ["serve", "-s", "out", "-l", "3000"]
