import logging
import socket
import threading

from Packets.Packet import *

# Define the IP address and port number of the Minecraft mod
MINECRAFT_MOD_IP = '127.0.0.1'  # Update with the actual IP address
MINECRAFT_MOD_PORT = 5000  # Update with the actual port number

global isAlive
isAlive = False


def _send_packet_thread_(packet: Packet):
    # Serialize the packet into JSON format
    packet_json = packet.ToJson()

    try:
        # Create a socket and connect it to the Minecraft mod
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
            sock.connect((MINECRAFT_MOD_IP, MINECRAFT_MOD_PORT))

            # Send the serialized packet with a newline character
            sock.sendall((packet_json + '\n').encode())

            # Receive the response from the Minecraft mod
            response = sock.recv(1024).decode()

            # Process the response
            return packet.ProcessResponse(response)

    except ConnectionRefusedError as e:
        return False
    except ConnectionResetError as e:
        logging.info(e)


def send_packet(packet: Packet):
    packet_thread = threading.Thread(target=_send_packet_thread_, args=[packet])
    packet_thread.start()


def _keep_alive_thread_():
    connect_packet_sent = False
    global isAlive
    dataTimer = 0
    while True:
        dataTimer += 1

        if isAlive and dataTimer >= 10 and connect_packet_sent:
            dataTimer = 0
            send_packet(PlayerDataPacket())

        isAlive = _send_packet_thread_(KeepAlivePacket())

        if isAlive and not connect_packet_sent:
            if _send_packet_thread_(ConnectPacket()):
                logging.info("Connected to minecraft server.")
                connect_packet_sent = True

        if not isAlive:
            connect_packet_sent = False
            logging.info("Trying to connect to minecraft server...")

        time.sleep(2 if isAlive else 10)

