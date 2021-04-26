import helper as db
#from influxdb import InfluxDBClient

json_body = [
    {
        "measurement": "cpu_load_short",
        "tags": {
            "host": "server01",
            "region": "us-west"
        },
        "time": "2009-11-10T23:00:00Z",
        "fields": {
            "value": 0.64
        }
    }
]

db.create_db()
client = db.get_connection()

json_body = [{"measurement":"battery","tags":{"status":"Discharging","source":"AC","health":"Good"},"time":"2021-04-06 20:31:15","fields":{"level":81,"temperature":25,"voltage":5}}]
print(type(json_body))
print(type(json_body[0]))

#client = InfluxDBClient('localhost', 8086, 'root', 'root', 'example')
#client.create_database('example')
client.write_points(json_body)
result = client.query('select * from battery;')
#print("Result: {0}".format(result))
print(result)


