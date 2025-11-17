### ============================
### STAGE 1: Build Angular
### ============================
FROM node:18-alpine AS build

# Diretório de trabalho
WORKDIR /app

# Copia arquivos de dependências primeiro (cache)
COPY package*.json ./

# Instala dependências
RUN npm install

# Copia o restante do projeto
COPY . .

# Build para produção
RUN npm run build -- --configuration production



### ============================
### STAGE 2: Nginx para servir o frontend
### ============================
FROM nginx:1.25-alpine

# Remove config padrão
RUN rm -rf /usr/share/nginx/html/*

# Copia o build do Angular para a pasta do Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Copia config customizada para single-page apps (SPA)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expõe porta
EXPOSE 80

# Comando final
CMD ["nginx", "-g", "daemon off;"]
