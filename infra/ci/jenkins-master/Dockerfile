FROM jenkins/jenkins:lts-slim

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN xargs /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY locale.groovy /usr/share/jenkins/ref/init.groovy.d/
