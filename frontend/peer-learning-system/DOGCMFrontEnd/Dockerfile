# 构建阶段
FROM node:14 AS build-stage

WORKDIR /app

# 安装依赖
COPY package*.json ./
RUN npm install

# 构建应用
COPY ./ .
RUN npm run build

# 运行阶段
# FROM nginx:stable-alpine as production-stage

# COPY --from=build-stage /app/dist /usr/share/nginx/html

# 指定运行时的端口
EXPOSE 80

CMD ["npm", "run", "serve"]
