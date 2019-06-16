pipeline {
  agent {
    label 'slave'
  }

  stages {
    stage('Login do private Docker registry') {
      steps {
        sh "echo 'doe' | docker login --username john --password-stdin https://host.docker.internal:5000"
      }
    }

    stage('Подготовка окружения') {
      steps {
        sh '''
        docker-compose --no-ansi -p $BUILD_TAG -f test/docker-compose.test.yml up -d
        '''
      }
    }

    stage('Создание информационной базы') {
      steps {
        echo 'Создание информационной базы...'
        sh '''
        docker-compose --no-ansi -p $BUILD_TAG -f test/docker-compose.test.yml exec -T test /opt/1C/v8.3/x86_64/1cv8 CREATEINFOBASE "Srvr=srv;Ref=demo;DBMS=PostgreSQL;DBSrvr=db;DB=demo;DBUID=postgres;DBPwd=;Locale=ru;CrSQLDB=Y;LicDstr=Y"
        '''
      }
    }

    stage('Инициализация базы') {
      steps {
        echo 'Инициализация базы...'
        sh '''
        docker-compose --no-ansi -p $BUILD_TAG -f test/docker-compose.test.yml exec -T test runner init-dev --ibconnection "/Ssrv/demo" --src=/src/cf
        '''
      }
    }

    stage('Синтаксическая проверка') {
      steps {
        echo 'Синтаксическая проверка...'
        sh '''
        docker-compose --no-ansi -p $BUILD_TAG -f test/docker-compose.test.yml exec -T test runner syntax-check --ibconnection "/Ssrv/demo"
        '''
      }
    }

    stage('Дымовое тестирование') {
      steps {
        echo 'Дымовое тестирование...'
        // TODO: Add tests
        // sh '''
        // docker-compose --no-ansi -p $BUILD_TAG -f test/docker-compose.test.yml exec -T test runner xunit --ibconnection "/Ssrv/demo" --settings /config/vRunner.json
        // '''
      }
    }

    stage('Сборка поставки') {
      steps {
        echo 'Сборка поставки...'
      }
    }
  }

  post {
    always {
      sh 'docker-compose --no-ansi -p $BUILD_TAG -f test/docker-compose.test.yml down -v'
    }
  }
}
