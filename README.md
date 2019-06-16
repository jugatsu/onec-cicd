# Описание

Данный проект представляет собой набор инструментов для локального развёртывания следующих продуктов:

1. [Jenkins](https://jenkins.io/)
1. [Gitea](https://gitea.io/en-us/)
1. [Private Docker Registry](https://docs.docker.com/registry/)
1. [gitsync](https://github.com/oscript-library/gitsync)
1. Вспомогательные утилиты [oscript](https://github.com/oscript-library)

Данные продукты чаще всего используются для построения конвейера непрерывной поставки конфигурации 1C:Предприятия.

# Развёртывание

:point_up: Предварительно скорректируйте файл `.env` под своё окружение:

```bash
cp .env.example .env
```

### Jenkins master

Первым делом необходимо развернуть `Jenkins`:

```bash
docker-compose -f infra/ci/docker-compose.ci.yml -p ci up -d jenkins
```

Дождаться пока `Jenkins` полностью будет готов к работе. Один из вариантов, посмотреть логи старта контейнера:

```bash
docker-compose -f infra/ci/docker-compose.ci.yml -p ci logs -f jenkins
```

при появлении надписи

```
INFO: Jenkins is fully up and running
```

Jenkins полностью готов к работе и доступен по адресу <http://localhost:8080>.

### Jenkins slave

Далее следует получить секретный ключ для подключения `slave` к мастер-ноде `Jenkins`.

Запускаем:

```bash
docker run --rm --entrypoint=sh byrnedo/alpine-curl -c 'curl -L -s -X GET \
    http://host.docker.internal:8080/computer/slave/slave-agent.jnlp \
    | sed "s/.*<application-desc main-class=\"hudson.remoting.jnlp.Main\"><argument>\([a-z0-9]*\).*/\1/"'
```

полученный секретный ключ экспортруем в переменную `JENKINS_SLAVE_SECRET`, например:

```bash
export JENKINS_SLAVE_SECRET=2ff4a384d18a5f151d9fac4841414054bfd6450bef411d798ec71208e0afc1d9
```

Запускаем `slave` и остальные сервисы:

```bash
docker-compose -f infra/ci/docker-compose.ci.yml -p ci up -d
```

### Git

Создадим учётную запись `onec` с паролем `Shee5i`:

```bash
docker-compose -f infra/ci/docker-compose.ci.yml -p ci exec -u git git \
 gitea admin create-user --name onec --email onec@localhost --admin -password Shee5i
```

Сервер Git доступен по адресу <http://localhost:3000>. Для авторизации используйте следующие учётные данные:

* `Username`: onec
* `Password`: Shee5i

### Private Docker Registry

Учётные данные для логина в приватный Docker Registry:

* `Username`: john
* `Password`: doe

```bash
echo 'doe' | docker login --username john --password-stdin localhost:5000
```

Отправка локальных образов в приватный Docker Registry:

```bash
docker push localhost:5000/onec-server
docker push localhost:5000/oscript-utils
```
