import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((socket.gethostname(), 7000))

msg ="Hello from Client 1"

s.send(bytes(msg, 'utf-8'))
s.close()


#while True:
#    buffer = s.recv(1024)

#if len(buffer) == 0:
#        break
#
#    msg += buffer.decode('UTF-8')

#print(msg)