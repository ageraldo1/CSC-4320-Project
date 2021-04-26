from influxdb import InfluxDBClient
from config import INFLUX_PARAMS

def create_db():
    client = InfluxDBClient(host=INFLUX_PARAMS['host'], port=INFLUX_PARAMS['port'], username=INFLUX_PARAMS['user'], password=INFLUX_PARAMS['password'], ssl=INFLUX_PARAMS['useSSL'])

    dbs = client.get_list_database()
    for db in dbs:
        if (db['name'] == INFLUX_PARAMS['database']):
            return

    print(f"Creating {INFLUX_PARAMS['database']} database...")
    client.create_database(INFLUX_PARAMS['database'])

def get_connection():
    return InfluxDBClient(host=INFLUX_PARAMS['host'], port=INFLUX_PARAMS['port'], username=INFLUX_PARAMS['user'], password=INFLUX_PARAMS['password'], ssl=INFLUX_PARAMS['useSSL'], database=INFLUX_PARAMS['database'])


