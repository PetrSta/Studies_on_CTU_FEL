from socket import *
import zlib
import time
import hashlib

receiverSocket = socket(AF_INET, SOCK_DGRAM)

BUFFER = 2048

UDP_IP_ADDRESS_RECEIVER = "147.32.216.61"
# UDP_IP_ADDRESS_RECEIVER = "127.0.0.1"
UDP_PORT_NO = 5555
UDP_IP_ADDRESS_SENDER = "147.32.218.224"
# UDP_IP_ADDRESS_SENDER = "127.0.0.1"
UDP_PORT_NO_SENDER = 4444

POSITIVE = 111111
NEGATIVE = 222222

FILE_HASH = ""
PREVIOUS_DATA = ""
FILE_NAME = ""
receiverSocket.bind((UDP_IP_ADDRESS_RECEIVER, UDP_PORT_NO))
receiverSocket.settimeout(5)


def receive_file_name(previous_data):
    potential_file_name, address_file = receiverSocket.recvfrom(BUFFER)
    print(potential_file_name[1024:])
    crc = int.from_bytes(potential_file_name[1024:], 'big')
    potential_file_name = potential_file_name[:1023]
    potential_file_name = potential_file_name.rstrip(b'\x00')
    print(potential_file_name)
    calculated_crc = zlib.crc32(potential_file_name)

    print(calculated_crc.to_bytes(BUFFER, byteorder='big'))
    print("CRC calculated, 1\n")
    if calculated_crc == crc:
        if potential_file_name != previous_data:
            if potential_file_name != b'':
                print("not empty")
                time.sleep(0.01)
                receiverSocket.sendto(POSITIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
                return potential_file_name, potential_file_name
            else:
                print("empty")
                time.sleep(0.01)
                receiverSocket.sendto(NEGATIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
                return "", ""
        else:
            print("repeated data")
            time.sleep(0.01)
            receiverSocket.sendto(POSITIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
            return "", ""
    else:
        print("wrong crc")
        print("Data: " + str(potential_file_name))
        print("Calculate CRC: " + str(calculated_crc) + " Received CRC: " + str(crc))
        time.sleep(0.01)
        receiverSocket.sendto(NEGATIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
        return "", ""


def receive_hash():
    received_hash, address_hash = receiverSocket.recvfrom(BUFFER)
    hash_crc = int.from_bytes(received_hash[1024:], 'big')
    received_hash = received_hash[:16]

    calculated_crc_hash = zlib.crc32(received_hash)
    print("CRC calculated, 2")
    if calculated_crc_hash == hash_crc:
        time.sleep(0.01)
        receiverSocket.sendto(POSITIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
        return received_hash, received_hash
    else:
        print("CRC HASH ERROR")
        time.sleep(0.01)
        receiverSocket.sendto(NEGATIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
        return "", ""


# listen for FILE_HASH
while FILE_HASH == "":
    FILE_HASH, PREVIOUS_DATA = receive_hash()
print(FILE_HASH)

# listen for FILE_NAME
while FILE_NAME == "":
    FILE_NAME, PREVIOUS_DATA = receive_file_name(PREVIOUS_DATA)
print(FILE_NAME)

writeFile = open(FILE_NAME, "wb")
counter = 0
control = True

while control:
    print("Listening...")
    try:
        data, address = receiverSocket.recvfrom(BUFFER)
        if len(data) == 2048:
            CRC = int.from_bytes(data[1025:], 'big')
            data = data[:1024]
        elif len(data) < 2048:
            CRC = int.from_bytes(data[len(data) - 1023:], 'big')
            data = data[:len(data) - 1024]
            print("____")
            print(CRC)
            print("____")
    except:
        if len(data) < 2048:
            CRC = int.from_bytes(data[len(data) - 1023:], 'big')
            data = data[:len(data) - 1024]
            print("____")
            print(CRC)
            print("____")

        control = False

    if data == PREVIOUS_DATA:
        time.sleep(0.01)
        receiverSocket.sendto(POSITIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))

    elif data != b'':
        calculatedCRC = zlib.crc32(data)
        print("CRC calculated, 3")
        if calculatedCRC == CRC:
            print("CRC compared = True")
            writeFile.write(data)
            counter += 1
            print("Packet " + str(counter) + " written")
            time.sleep(0.01)
            receiverSocket.sendto(POSITIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
            PREVIOUS_DATA = data
        else:
            print("CRC compared = False")
            print(len(data))
            print("Received CRC " + str(CRC) + " Calculated CRC " + str(calculatedCRC))
            print(data)
            time.sleep(0.01)
            receiverSocket.sendto(NEGATIVE.to_bytes(BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))

    if data == b'':
        writeFile.write(data)
        counter += 1
        control = False

print("Transfer ended.")
print("Received " + str(counter) + " packets")
print("------------------------------------------------")
print("RECEIVED HASH: " + str(FILE_HASH))
print("------------------------------------------------")
writeFile.close()

fileForHash = open(FILE_NAME, "rb")

CALCULATED_HASH = hashlib.md5(fileForHash.read()).digest()
print("CALCULATED HASH: " + str(CALCULATED_HASH))

if CALCULATED_HASH != FILE_HASH:
    print("File was not received correctly.\n")

fileForHash.close()
receiverSocket.close()
