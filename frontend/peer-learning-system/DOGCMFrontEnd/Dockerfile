# 构建阶段
FROM node:18

WORKDIR /app

# 安装依赖
COPY package*.json ./
RUN npm install

# 构建应用
COPY ./ ./

# 指定运行时的端口
EXPOSE 10087

CMD ["npm", "run", "serve"]
