#!/bin/sh
echo "Grafana Server : $GRAFANA"
ping $GRAFANA -c 3

echo "Loading Grafana customization...."
echo "Creating demo account..."
curl -s -X POST -H "Content-Type: application/json" --data @create-demo-account.json http://admin:admin@$GRAFANA:3000/api/admin/users

echo "Creating InfluxDB datasource..."
curl -s -X POST -H "Content-Type: application/json" --data @create-influx-ds.json http://admin:admin@$GRAFANA:3000/api/datasources
echo ""

echo "Loading dashboards..."
curl -s -X POST -H "Content-Type: application/json" --data @battery_status.json http://admin:admin@$GRAFANA:3000/api/dashboards/db
echo ""


echo "Setting user preferences..."
curl -s -X POST -H "Content-Type: application/json" http://admin:admin@$GRAFANA:3000/api/user/stars/dashboard/1
echo ""

curl -s -X PUT -H "Content-Type: application/json" --data @user-preferences.json http://demo:demo@$GRAFANA:3000/api/user/preferences
echo ""

while :; 
  do :; 
done & kill -STOP $! && wait $!

