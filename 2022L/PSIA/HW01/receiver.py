from socket import *

receiverSocket = socket(AF_INET, SOCK_DGRAM)
BUFFER = 1024
UDP_IP_ADDRESS = "147.32.221.15"
UDP_PORT_NO = 5555

receiverSocket.bind((UDP_IP_ADDRESS, UDP_PORT_NO))
data, address = receiverSocket.recvfrom(BUFFER)
fileName = data
writeFile = open(fileName, "wb")
counter = 1

while True:
    print("Listening...")
    data, address = receiverSocket.recvfrom(BUFFER)
    writeFile.write(data)
    if data != b'':
        counter += 1
    if data == b'':
        break

print("Transfer ended.")
print("Received " + str(counter) + " packets")
writeFile.close()
receiverSocket.close()
