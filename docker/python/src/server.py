import socket
import threading
import json
import helper as db
from config import SERVER_SOCKET

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind( (SERVER_SOCKET['host'], SERVER_SOCKET['port']) )
sock.listen(SERVER_SOCKET['queueSize'])

def handler(c, a):
    payload = ""

    while True:
        buffer = c.recv(1024)

        if (len(buffer) == 0):
            c.close()
            break

        payload += buffer.decode('utf-8')

    print(f'[{threading.get_ident()}:{threading.current_thread().name}] Data Received from client {a}: {payload}')

    client = db.get_connection()
    client.write_points([json.loads(payload)])

while True:
    db.create_db()
    conn, addr = sock.accept()

    sock_thread = threading.Thread(target=handler, args=(conn, addr))
    sock_thread.daemon = True
    sock_thread.start()

    print(f'Connection to {addr} established')
    
