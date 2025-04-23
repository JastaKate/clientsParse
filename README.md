# Докеризация приложения

**Dockerfile** — это текстовый файл который содержит набор инструкций, следуя которым Docker будет собирать образ нашего приложения и запускать контейнер. В Dockerfile мы как бы создаем свой образ на основе других, только добавляем к ним недостающие части которые уже нужные нам 

**Минимум что нужно чтобы создать образ нашего приложения:**
- Какая-нибудь ОС 
- Доп. библиотеки чтобы приложение вообще могла запуститься - среда исполнения кода
- Сам исполняемый файл нашего приложения
- Какая-то команда запуска этого всего

Для примера возьмем простое приложение, которое парсит данные о клиентах магазина и их адресах из .xml файлов и кладет эти данные в базу. Наше приложение будет иметь и бэкенд часть и фронтенд, где и можно будет увидеть список наших распарсенных пользователей. 

### Backend часть

Пусть в нашем приложении контроллеры - ClientsController для работы со списком клиентов и AddressesController для работы со списком адресов. Для хранения распарсенных данных будем использовать PostgreSQL.

Теперь мы хотим собрать образ нашей бэкенд части. Для этого нам нужно ОС (Linux), JDK, .jar файл нашего сервиса. Прежде чем создать Dockerfile разберем команды, которые будем использовать 

| Команда          | Описание         |
|------------------|------------------|
| `FROM <image>` | указываем базовый образ на основе которого мы хотим собирать наш|
| `WORKDIR <directory>`|создает папку, которая будет рабочей дерикторией внутри нашего образа. Все будущие инструкции нашего dokerfile будут исполняться внутри этой папки |
|`COPY <src> <dest>`|копирует наше приложение в image|
|`ENV  <key>=<value>`|устанавливает переменные среды|
|`EXPOSE <port>`|определяет , что контейнер будет работать с сервисом, который слушает определенный порт|
|`RUN <command>`|нужна, чтобы выполнить любую команду внутри контейнера во время сборки образа|
|`USER <user>[:<group>]`|нужна, чтобы указать, под каким пользователем должны выполняться команды внутри контейнера|

>🔴 **ВАЖНО:** По умолчанию контейнеры работают от root (суперпользователя), что небезопасно. Поэтому USER переключает контейнер на указанного пользователя. 
Это уменьшает риски, если контейнер будет взломан (не будет прав root). Пользователь должен существовать, его можно создать с помощью RUN adduser и добавить ему группу с помощью RUN addgroup

| Команда          | Описание         |
|------------------|------------------|
|`ENTRYPOINT`|команда которая запустит наше приложение из образа куда мы его скопировали|
|`CMD`|задаёт параметры по умолчанию, которые передаются в ENTRYPOINT (если он есть), или исполняются как команда, если ENTRYPOINT нет вообще. Эти аргументы можно переопределить при запуске docker run|

>🟦 **Примечание:** Dockerfile должен содержать хотя бы одну из инструкций CMD или ENTRYPOINT.  ENTRYPOINT используем если хотим чтобы всегда запускалась именно одна команда, а CMD если хотим иметь возможность переопределять команду при запуске контейнера с аргументами. Также ENTRYPOINT невозможно случайно заменить при запуске контейнера — только можно добавить аргументы

Таким образом наш Dockerfile для бэкенда будет выглядеть так:
```yaml
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY build/libs/*.jar ./app.jar
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser && \
    chown -R appuser:appuser /app
USER appuser
CMD ["java", "-jar", "app.jar"]
```

Выполняем команду ниже и наш образ готов
>  docker build -t «название нашего приложения» . 

Теперь нужно запустить контейнер с нашим приложением на основе созданного образа. Для этого создадим docker-compose.yml
```yaml
version: '3'

name: clients-parser

services:
  clients-parse-service:
    image: clients-parse:latest
    depends_on:
      - postgres
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_USER}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD}
    expose:
      - "8080"
```
Тут clients-parse:latest - образ который мы создали. Задаем переменные окружения(это безопаснее чем просто захардкодить их прямо в docker-compose.yaml). Для этого в корне проекта создадим файл .env в котором прописываем значения

Благодаря *expose* контейнер будет доступен только внутри docker-сети для нашего фронтенда

> 🔴 **ВАЖНО:** нужно не забыть добавить .env в gitingnore, чтобы не закоммитить чувствительные данные

Также прописываем зависимость от postgres - Docker Compose сначала запустит postgres, а только потом clients-parse-service. Соответсвенно, нужно еще создать контейнер для postgres: 
```yaml
postgres:
  image: postgres:11.1
  restart: unless-stopped
  environment:
    POSTGRES_USER: ${POSTGRES_USER}
    POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    POSTGRES_DB: ${POSTGRES_DB}
  volumes:
    - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```
Тут также создаётся volume postgres-data, который монтируется в /var/lib/postgresql/data внутри контейнера (где PostgreSQL хранит свои данные). Это нужно чтобы при перезапуске контейнера не потерять данные 


### Frontend часть

Часть фронтенда сделаем максимально простой, просто чтобы показать взаимодействие с API бэкенда. Создадим новую директорию *frontend* в корне проекта и в ней выполним команду, которая создаст React-проект с использованием сборщика Vite:
> npm create vite@latest . -- --template react 

Для демонстрации возьмем метод *getClientsByStreet(@RequestParam street: String)* из контроллера *ClientsController*, пропишем для него *App.jsx* и *vite.config.js*, чтобы просто выводить список пользователей указанной улицы на экран.
Получается так:
![image](https://github.com/user-attachments/assets/98357109-406f-46cf-bc22-d87068ae7889)

Также создадим конфигурацию Nginx:
```yaml
server {
  listen 80;

  location / {
    root /usr/share/nginx/html;
    index index.html;
    try_files $uri /index.html;
  }

  location /api/ {
    proxy_pass http://clients-parse-service:8080; // <-- перенаправление на бэк
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
  }
}
```

Теперь можно создавать Dockerfile для фронтенда: 
```yaml
FROM node:20-alpine AS build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY /frontend/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```
Используются уже знакомые нам команды, отличие лишь в том, что тут 2 стадии сборки образа. Это нужно для уменьшения размера образа, оптимизации слоев и исключения из финального образа dev-зависимостей (для безопасности!)

Теперь доработать наш docker-compose. Но сначала нужно поговорить о типах сетей в Doсker, чтобы понять как наши контейнеры бэкенда и фронтенда будут взаимодействовать

### Типы сетей в Doсker
#### 📍 Bridge (docker0 172.17.0.0/16)
Тип сети по умолчанию, при обычном запуске контейнеров (обычный docker run) все они попадают в эту сеть. Контейнеры с таким типом сети имеют доступ «наружу», т.е могут подключаться к другим серверам, интернету и тд. Также мы может подлючиться к этим контейнерам локально, пробросив порты (-p 80:80 - соединяем порт хоста и порт контейнера)

В сети типа *bridge* контейнеры могут общаться между собой (наш случай с фронтом и бэком) - они получают рандомные ip-адреса и по ним общаются, а чтобы они могли общаться по именам контейнеров нужно создать не дефолтную сеть и поместить контейнеры в нее

>🟦 **Примечание:** это правило работает для дефолтной сети, когда контейнеры запускаются через docker run. Это не работает для контейнеров, которые запущены вместе в docker-compose - там включён встроенный DNS, который разрешает имена контейнеров (имена контейнеров становятся DNS-именами) и можно общаться не по ip-адресам, а по именам контейнеров 

> 🔴 **ВАЖНО:** контейнеры из разных сетей не могут общаться между собой! Это хорошо тем, что мы можем изолировать два приложения друг от друга, что добавляет безопасности

#### 📍 Host (ServerIP 10.15.11.12)
При запуске контейнера нужно указать этот тип сети. Такие контейнеры получают IP хоста, также имеют доступ «наружу», а чтобы подключиться локально указываем адрес хоста и порт контейнера (например 10.15.11.12:80)

В этой сети контейнеры также могут общаться между собой, но они не получают себе рандомные ip-адреса, а используют ip-адрес хоста 

> 🔴 **ВАЖНО:** можно создать только одну сеть типа Host

#### 📍 None 
При запуске контейнера нужно указать этот тип сети. С таким типом сети вообще никак не сможем подключиться к контейнеру - ни снаружи, ни локально

В этой сети контейнеры изолированы друг от друга, не могут общаться между собой 

> 🔴 **ВАЖНО:** можно создать только одну сеть типа None

Мы будет использовать сеть типа Bridge, которую создадим в docker-compose:
```yaml
networks:
    - app-network
```
Ее можно увидеть, если выполнить команду 
>docker network ls

![image](https://github.com/user-attachments/assets/a2aa7164-9194-4b59-a973-5d1e118e6750)

### Итоговый docker-compose

```yaml
version: '3'

name: clients-parser

services:
  postgres:
    image: postgres:11.1
    restart: unless-stopped
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - app-network

  clients-parse-service:
    image: clients-parse:latest
    depends_on:
      - postgres
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_USER}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD}
    expose:
      - "8080"
    networks:
      - app-network

  clients-parse-frontend:
    image: frontend:latest
    ports:
      - "8090:80"
    depends_on:
      - clients-parse-service
    networks:
      - app-network

volumes:
  postgres-data:

networks:
  app-network:
    driver: bridge
```
