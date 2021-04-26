FROM alpine:3.7

RUN apk add --no-cache curl

ENV GRAFANA=${GRAFANA}

USER root
WORKDIR /app

COPY battery_status.json /app
COPY create-demo-account.json /app
COPY create-influx-ds.json /app
COPY user-preferences.json /app
COPY run.sh /app

RUN chmod +x /app/run.sh 


ENTRYPOINT [ "/app/run.sh" ]
