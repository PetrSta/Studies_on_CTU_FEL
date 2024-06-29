from socket import *
import zlib
import hashlib
receiverSocket = socket(AF_INET, SOCK_DGRAM)

SECONDARY_BUFFER = 2048
MAIN_BUFFER = 2052

UDP_IP_ADDRESS_RECEIVER = "147.32.218.138"
# UDP_IP_ADDRESS_RECEIVER = "147.32.216.113"
UDP_PORT_NO = 5555
UDP_IP_ADDRESS_SENDER = "147.32.219.61"
# UDP_IP_ADDRESS_SENDER = "147.32.216.232"
UDP_PORT_NO_SENDER = 4444

POSITIVE = 111111
NEGATIVE = 222222

FILE_HASH = ""
PREVIOUS_DATA = ""
FILE_NAME = ""
FILE_SIZE = 0
receiverSocket.bind((UDP_IP_ADDRESS_RECEIVER, UDP_PORT_NO))
receiverSocket.settimeout(7)
packet_dict = {}


def receive_hash():
    received_hash, address_hash = receiverSocket.recvfrom(SECONDARY_BUFFER)
    hash_crc = int.from_bytes(received_hash[1024:], 'big')
    received_hash = received_hash[:16]

    calculated_crc_hash = zlib.crc32(received_hash)
    if calculated_crc_hash == hash_crc:
        receiverSocket.sendto(POSITIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
        return received_hash, received_hash
    else:
        receiverSocket.sendto(NEGATIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
        return "", ""


def receive_file_name(previous_data):
    potential_file_name, address_file = receiverSocket.recvfrom(SECONDARY_BUFFER)
    crc = int.from_bytes(potential_file_name[1024:], 'big')
    potential_file_name = potential_file_name[:1024]
    potential_file_name = potential_file_name.rstrip(b'\x00')
    calculated_crc = zlib.crc32(potential_file_name)
    if calculated_crc == crc:
        if potential_file_name != previous_data:
            if potential_file_name != b'':
                receiverSocket.sendto(POSITIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                                   UDP_PORT_NO_SENDER))
                return potential_file_name, potential_file_name
            else:
                receiverSocket.sendto(NEGATIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                                   UDP_PORT_NO_SENDER))
                return "", ""
        else:
            receiverSocket.sendto(POSITIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                               UDP_PORT_NO_SENDER))
            return "", ""
    else:
        receiverSocket.sendto(NEGATIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                           UDP_PORT_NO_SENDER))
        return "", ""


def receive_file_size(previous_data):
    potential_file_size, size_address = receiverSocket.recvfrom(SECONDARY_BUFFER)
    crc = int.from_bytes(potential_file_size[1024:], 'big')
    potential_file_size = potential_file_size[:1024]
    calculated_crc = zlib.crc32(potential_file_size)
    if calculated_crc == crc:
        if potential_file_size != previous_data:
            if potential_file_size != b'':
                receiverSocket.sendto(POSITIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                                   UDP_PORT_NO_SENDER))
                return potential_file_size, potential_file_size
            else:
                receiverSocket.sendto(NEGATIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                                   UDP_PORT_NO_SENDER))
                return 0, previous_data
        else:
            receiverSocket.sendto(POSITIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                               UDP_PORT_NO_SENDER))
            return 0, previous_data
    else:
        receiverSocket.sendto(NEGATIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                           UDP_PORT_NO_SENDER))
        return 0, previous_data


def add_data_to_list(selective_repeat_data):
    index = int.from_bytes(selective_repeat_data[:4], 'big')
    data = selective_repeat_data[4:len(selective_repeat_data) - 1024]
    crc = int.from_bytes(selective_repeat_data[len(selective_repeat_data) - 1024:], 'big')
    ####
    crc_data = selective_repeat_data[:len(selective_repeat_data) - 1024]
    calculated_crc = zlib.crc32(crc_data)
    print("Calculated crc: " + str(calculated_crc))
    print("Received crc: " + str(crc))
    print("Index is: " + str(index))
    ####
    if crc == calculated_crc:
        packet_dict[index] = data


# listen for FILE_HASH
while FILE_HASH == "":
    FILE_HASH, PREVIOUS_DATA = receive_hash()
print("__FILE_HASH__")
print(FILE_HASH)
print("_____")

# listen for FILE_NAME
while FILE_NAME == "":
    FILE_NAME, PREVIOUS_DATA = receive_file_name(PREVIOUS_DATA)
print("__FILE_NAME:__")
print(FILE_NAME)
print("_____")

# listen for PACKET_WINDOW
while FILE_SIZE == 0:
    FILE_SIZE, PREVIOUS_DATA = receive_file_size(PREVIOUS_DATA)
print("__FILE_SIZE:__")
FILE_SIZE = int.from_bytes(FILE_SIZE, 'big')
print(FILE_SIZE)
print("_____")

print("Opening file")
writeFile = open(FILE_NAME, "wb")

sentAck = 0
ackToSend = 0
frame_size = 10
ackData = 0
ackResends = 0
is_first_packet = True
while sentAck != FILE_SIZE + 1:
    receiverSocket.settimeout(1)
    for i in range(frame_size):
        try:
            data, address = receiverSocket.recvfrom(2052)
        except:
            if is_first_packet:
                receiverSocket.sendto(POSITIVE.to_bytes(SECONDARY_BUFFER, 'big'), (UDP_IP_ADDRESS_SENDER,
                                                                                   UDP_PORT_NO_SENDER))
                continue
            if ackData != 0:
                receiverSocket.sendto(ackData, (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
            ackResends += 1
            i -= 1
            if ackResends > 2:
                break
            else:
                continue
        add_data_to_list(data)
        is_first_packet = False

    sorted_dict_indexes = sorted(packet_dict)
    for i in range(sentAck, frame_size + sentAck):
        if i in sorted_dict_indexes:
            writeFile.write(packet_dict.get(i))
            if i == (frame_size+sentAck-1):
                print("FULL FRAME RECEIVED")
                ackToSend = i+1
        if i not in sorted_dict_indexes:
            ackToSend = i
            break
    # send ack
    ackData = ackToSend.to_bytes(1024, "big")
    ackCrc = zlib.crc32(ackData)
    ackData = ackData + ackCrc.to_bytes(1024, "big")
    receiverSocket.sendto(ackData, (UDP_IP_ADDRESS_SENDER, UDP_PORT_NO_SENDER))
    ackResends = 0
    sentAck = ackToSend
    print("ACK NOW HERE: " + str(sentAck))

print("Transfer ended.")

print("------------------------------------------------")
print("RECEIVED HASH: " + str(FILE_HASH))
print("------------------------------------------------")
writeFile.close()

fileForHash = open(FILE_NAME, "rb")

CALCULATED_HASH = hashlib.md5(fileForHash.read()).digest()
print("CALCULATED HASH: " + str(CALCULATED_HASH))

if CALCULATED_HASH != FILE_HASH:
    print("File was not received correctly.")

fileForHash.close()
receiverSocket.close()
